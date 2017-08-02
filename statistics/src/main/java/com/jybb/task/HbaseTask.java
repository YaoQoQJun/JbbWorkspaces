package com.jybb.task;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.stereotype.Component;

import com.jybb.jbbServicesMapper.MeelIngredientTaskMapper;
import com.jybb.jbbServicesMapper.MembersMapper;
import com.jybb.mapper.AppEventMapper;
import com.jybb.mapper.DisableMapper;
import com.jybb.mapper.ExtensionMapper;
import com.jybb.mapper.HbaseMapper;
import com.jybb.pojo.Disable;
import com.jybb.pojo.Extension;
import com.jybb.pojo.HbaseDetail;
import com.jybb.pojo.HbaseDisable;
import com.jybb.pojo.HbaseExtension;

@Component
public class HbaseTask {

	private Logger logger = LoggerFactory.getLogger(HbaseTask.class);
	@Autowired
	private HbaseTemplate htemplate;

	@Autowired
	private ExtensionMapper extensionMapper;

	@Autowired
	private HbaseMapper hbaseMapper;

	@Autowired
	private DisableMapper disableMapper;
	
	@Autowired
	private MeelIngredientTaskMapper meelIngredientTaskMapper;
	
	@Autowired
	private AppEventMapper appEventMapper;
	
	@Autowired
	private MembersMapper membersMapper;

	/**
	 * 每一个小时加载所有网站 pv uv ip量
	 */
	public void doExtensionJob() {
		try {
			System.out.println("每一个小时加载所有网站 pv uv ip量--------------doExtensionJob");
			List<Extension> extensions = extensionMapper.findExtensions();
			Date date1 = new Date(getHourTime(System.currentTimeMillis(),
					new Date().getHours() - 1));
			Date date2 = new Date(getHourTime(date1.getTime(),
					date1.getHours() + 1));
			long start = date1.getTime();
			long end = date2.getTime();
			for (int i = 0; i < extensions.size(); i++) {
				Extension extension = extensions.get(i);
				Integer pv = getPVs(extension, start, end);
				Integer uv = getUVs(extension, start, end);
				Integer ip = getIPs(extension, start, end);

				HbaseExtension hbaseExtension = new HbaseExtension();
				hbaseExtension.setExtension_id(extension.getId());
				hbaseExtension.setExtension_link(extension.getExtension_link());
				hbaseExtension.setPv(pv);
				hbaseExtension.setUv(uv);
				hbaseExtension.setIp(ip);
				hbaseExtension.setStart(start);
				hbaseExtension.setEnd(end);
				hbaseMapper.saveExtension(hbaseExtension);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 每一个小时加载禁用网站 pv uv ip量
	 */
	public void doDisableJob() throws IOException {
		System.out.println("每一个小时加载禁用网站 pv uv ip量--------------doDisableJob");
		List<Disable> disables = disableMapper.findDisables();

		Date date1 = new Date(getHourTime(System.currentTimeMillis(),
				new Date().getHours() - 1));
		Date date2 = new Date(
				getHourTime(date1.getTime(), date1.getHours() + 1));
		long start = date1.getTime();
		long end = date2.getTime();

		for (int i = 0; i < disables.size(); i++) {

			Disable disable = disables.get(i);
			Integer pv = getPVs(disable, start, end);
			Integer uv = getUVs(disable, start, end);
			Integer ip = getIPs(disable, start, end);

			HbaseDisable hbaseDisable = new HbaseDisable();
			hbaseDisable.setDisable_id(disable.getId());
			hbaseDisable.setDisable_link(disable.getDisable_link());

			hbaseDisable.setPv(pv);
			hbaseDisable.setUv(uv);
			hbaseDisable.setIp(ip);
			hbaseDisable.setStart(start);
			hbaseDisable.setEnd(end);

			try {
				hbaseMapper.saveDisable(hbaseDisable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 每一个小时加载网站详情链接的 pv.
	 */
	public void doDetail_pv_job() {
		try {
			System.out.println("每一个小时加载网站详情链接的 pv.--------------doDetail_pv_job");
			Date date1 = new Date(getHourTime(System.currentTimeMillis(),
					new Date().getHours() - 1));
			Date date2 = new Date(getHourTime(date1.getTime(),
					date1.getHours() + 1));
			long start = date1.getTime();
			long end = date2.getTime();

			final Long time = getStartTime(System.currentTimeMillis());

			Scan scan = new Scan();
			scan.setTimeRange(start, end);

			htemplate.find("dmp_pv", scan, new ResultsExtractor<Boolean>() {
				public Boolean extractData(ResultScanner rs) throws Exception {
					for (Result result : rs) {
						for (KeyValue kv : result.raw()) {
							if ("url".equals(new String(kv.getQualifier()))) {
								hbaseMapper.updateHbaseDetail_pvs(
										new String(kv.getValue()), time);
								break;
							}
						}
					}
					return true;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 每一个小时加载网站详情链接的 uv.
	 */
	public void doDetail_uv_job() {
		System.out.println("每一个小时加载网站详情链接的 uv.--------------doDetail_uv_job");
		try {

			Date date1 = new Date(getHourTime(System.currentTimeMillis(),
					new Date().getHours() - 1));
			Date date2 = new Date(getHourTime(date1.getTime(),
					date1.getHours() + 1));
			long start = date1.getTime();
			long end = date2.getTime();

			final Long time = getStartTime(System.currentTimeMillis());

			Scan scan = new Scan();
			scan.setTimeRange(start, end);

			htemplate.find("dmp_uv", scan, new ResultsExtractor<Boolean>() {
				public Boolean extractData(ResultScanner rs) throws Exception {
					for (Result result : rs) {
						for (KeyValue kv : result.raw()) {
							if ("j_url".equals(new String(kv.getQualifier()))) {
								hbaseMapper.updateHbaseDetail_uvs(
										new String(kv.getValue()), time);
								break;
							}
						}
					}
					return true;
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 每一个小时加载网站详情链接的 ip.
	 */
	public void doDetail_ip_job() {
		System.out.println("每一个小时加载网站详情链接的 ip.--------------doDetail_ip_job");
		try {

			final Long time = getStartTime(System.currentTimeMillis());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			Calendar c1 = Calendar.getInstance();
			Date d1 = c1.getTime();
			String tableName = "dmp_uv_" + sdf.format(d1);

			List<HbaseDetail> hbaseDetails = hbaseMapper.findHbaseDetail(time);

			for (int i = 0; i < hbaseDetails.size(); i++) {
				HbaseDetail hbaseDetail = hbaseDetails.get(i);
				String url = hbaseDetail.getUrl();
				List<String> ips = hbaseMapper.countIps(url, time, tableName);
				hbaseMapper.updateHbaseDetail_ips(url, time, ips.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 每一个小时加载网站详情链接的 平均访问时间.
	 */
	public void doDetail_at_job() {
		System.out.println("每一个小时加载网站详情链接的 平均访问时间.--------------doDetail_at_job");
		try {

			Date date1 = new Date(getHourTime(System.currentTimeMillis(),
					new Date().getHours() - 1));
			Date date2 = new Date(getHourTime(date1.getTime(),
					date1.getHours() + 1));
			long start = date1.getTime();
			long end = date2.getTime();

			Scan scan = new Scan();
			scan.setTimeRange(start, end);

			final Long time = getStartTime(System.currentTimeMillis());
			final List<Map<String, String>> list = new ArrayList<Map<String, String>>();

			htemplate.find("dmp_pv", scan, new ResultsExtractor<Boolean>() {
				public Boolean extractData(ResultScanner rs) throws Exception {
					for (Result result : rs) {

						Map<String, String> map = new HashMap<String, String>();

						for (KeyValue kv : result.raw()) {

							if ("url".equals(new String(kv.getQualifier()))) {
								map.put("url", new String(kv.getValue()));
							}

							if ("sessionID".equals(new String(kv.getQualifier()))) {
								map.put("sessionID", new String(kv.getValue()));
							}

							if ("isVisit".equals(new String(kv.getQualifier()))) {
								map.put("isVisit",
										(Integer.parseInt(new String(kv
												.getValue())) + 1) + "");
							}

							if ("ctime".equals(new String(kv.getQualifier()))) {
								map.put("ctime", new String(kv.getValue()));
							}

						}

						list.add(map);
					}
					return true;
				}
			});
			
			Date date3 = new Date(getHourTime(date1.getTime(),
					date1.getHours() + 2));

			for (int i = 0; i < list.size(); i++) {

				Map<String, String> map = list.get(i);
				double at = getAverageTime(map.get("sessionID"),
						map.get("isVisit"), map.get("ctime"), start,
						date3.getTime());

				if (at != 0.0) {
					String ats = hbaseMapper.getHbaseDetail_ats(map.get("url"),
							time);
					if (ats == null) {
						continue;
					}
					String[] strs = ats.split("-");
					Double a = Double.parseDouble(strs[0])
							* Integer.parseInt(strs[1]);

					double b = (a + at) / (Integer.parseInt(strs[1]) + 1);

					BigDecimal bigDecimal = new BigDecimal(b);
					double f1 = bigDecimal
							.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					if(f1<0){
						f1=0;
					}
					String ats2 = f1 + "-" + (Integer.parseInt(strs[1]) + 1);

					hbaseMapper.updateHbaseDetail_ats(map.get("url"), time,
							ats2);

					hbaseMapper.updateHbaseDetail_op(map.get("url"), time);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 每一个小时 加载hbase中的UV数据，保存到本地mysql数据库
	 */
	public void doGetDatas() {
		System.out.println("每一个小时 加载hbase中的UV数据，保存到本地mysql数据库.--------------doGetDatas");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			Calendar c1 = Calendar.getInstance();
			Date d1 = c1.getTime();
			final String tableName = "dmp_uv_" + sdf.format(d1);

			Date date1 = new Date(getHourTime(System.currentTimeMillis(),
					new Date().getHours() - 1));
			Date date2 = new Date(getHourTime(date1.getTime(),
					date1.getHours() + 1));
			long start = date1.getTime();
			long end = date2.getTime();
			Scan scan = new Scan();
			scan.setTimeRange(start, end);

			final Long time = getStartTime(System.currentTimeMillis());

			// 推广链接的UV
			htemplate.find("dmp_uv", scan, new ResultsExtractor<Boolean>() {
				public Boolean extractData(ResultScanner rs) throws Exception {
					for (Result result : rs) {

						Map<String, String> map = new HashMap<String, String>();
						for (KeyValue kv : result.raw()) {
							map.put(new String(kv.getQualifier()), new String(
									kv.getValue()));
						}

						map.put("tableName", tableName);
						// 保存新访问的url
						HbaseDetail hbaseDetail = hbaseMapper
								.findHbaseDetailByUrlAndTime(map.get("j_url"),
										time);
						if (hbaseDetail == null) {
							hbaseDetail = new HbaseDetail();
							hbaseDetail.setUrl(map.get("j_url"));
							hbaseDetail.setTime(time);
							hbaseDetail.setCount_pv(0);
							hbaseDetail.setCount_uv(0);
							hbaseDetail.setCount_ip(0);
							hbaseDetail.setCount_ov(0.0);
							hbaseDetail.setCount_at("0-0");
							hbaseDetail.setCount_br(0.0);
							hbaseDetail.setCount_op(0);
							hbaseMapper.saveHbaseDetail(hbaseDetail);
						}

						hbaseMapper.saveDatasMap(map);
					}
					return true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 每个月的1号凌晨 ，创建UV数据表
	 */
	public void doCreateTable() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar c1 = Calendar.getInstance();
		Date d1 = c1.getTime();
		String tableName = "dmp_uv_" + sdf.format(d1);
		hbaseMapper.createDataTable(tableName);
	}

	
	/**
	 * 每天凌晨1点，统计事件，昨日完成营养膳食任务的人数。
	 */
	public void doCountMeelIngredientTask(){
		try {
			System.out.println("每天凌晨1点，统计事件，昨日完成营养膳食任务的人数。--------------doCountMeelIngredientTask");
			long a=System.currentTimeMillis()-1000*60*60*24;
			List<HashMap<String, Object>> list=meelIngredientTaskMapper.userPeriodDetailCount(
					(int)(getStartTime(a)/1000), 
					(int)(getEndTime(a)/1000));
			
			for (int i = 0; i < list.size(); i++) {
				HashMap<String,Object> map=list.get(i);
				String uid=map.get("uid").toString();
				String periodDetail=map.get("period_detail").toString();
				String userCount=map.get("count")+"";
				
				String count=meelIngredientTaskMapper.periodDetailCount(periodDetail);
				if(userCount.equals(count)||Integer.parseInt(userCount)==4){
					HashMap<String,String> memberMap=membersMapper.getMemberByUid(Integer.parseInt(uid));
					HashMap<String,String> appEventMap=new HashMap<String,String>();
					
					appEventMap.put("client_id", uid);
					
					if(memberMap.get("regfrom")==null){
						appEventMap.put("client_model", "0");
					}else{
						appEventMap.put("client_model", memberMap.get("regfrom"));
					}
					
					if(memberMap.get("regchannel")==null){
						appEventMap.put("channel_name", "0");
					}else{
						appEventMap.put("channel_name", memberMap.get("regchannel"));
					}
					
					appEventMap.put("app_version", "0");
					appEventMap.put("event_name", "diet_task");
					appEventMap.put("time", a+"");
					
					appEventMapper.AddAppEvent(appEventMap);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private Integer getPVs(Object obj, long t1, long t2) throws IOException {
		Extension extension = null;
		Disable disable = null;
		if (obj instanceof Extension) {
			extension = (Extension) obj;
		} else if (obj instanceof Disable) {
			disable = (Disable) obj;
		}

		Scan scan = null;
		scan = new Scan();
		scan.setTimeRange(t1, t2);
		FilterList filterList1 = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);
		RegexStringComparator comp = null;

		if (extension != null) {
			comp = new RegexStringComparator("^"
					+ extension.getExtension_link() + ".*");
		} else {
			comp = new RegexStringComparator("^" + disable.getDisable_link()
					+ ".*");
		}

		filterList1.addFilter(new SingleColumnValueExcludeFilter("user"
				.getBytes(), "url".getBytes(), CompareOp.EQUAL, comp));

		scan.setFilter(filterList1);

		int count = htemplate.find("dmp_pv", scan,
				new ResultsExtractor<Integer>() {
					public Integer extractData(ResultScanner rs)
							throws Exception {
						int count = 0;
						for (Result result : rs) {
							count++;
						}
						return count;
					}
				});
		return count;
	}

	private Integer getUVs(Object obj, long t1, long t2) throws IOException {

		Extension extension = null;
		Disable disable = null;
		if (obj instanceof Extension) {
			extension = (Extension) obj;
		} else if (obj instanceof Disable) {
			disable = (Disable) obj;
		}

		Scan scan = null;
		// 推荐链接uv量查询，根据url,渠道id
		scan = new Scan();
		scan.setTimeRange(t1, t2);
		FilterList filterList1 = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		RegexStringComparator comp = null;
		if (extension != null) {
			comp = new RegexStringComparator("^"
					+ extension.getExtension_link() + ".*");
		} else {
			comp = new RegexStringComparator("^" + disable.getDisable_link()
					+ ".*");
		}

		filterList1.addFilter(new SingleColumnValueExcludeFilter("info"
				.getBytes(), "j_url".getBytes(), CompareOp.EQUAL, comp));

		scan.setFilter(filterList1);

		int count = htemplate.find("dmp_uv", scan,
				new ResultsExtractor<Integer>() {
					public Integer extractData(ResultScanner rs)
							throws Exception {
						int count = 0;
						for (Result result : rs) {
							count++;
						}
						return count;
					}
				});

		return count;
	}

	private Integer getIPs(Object obj, long t1, long t2) throws IOException {

		Extension extension = null;
		Disable disable = null;
		if (obj instanceof Extension) {
			extension = (Extension) obj;
		} else if (obj instanceof Disable) {
			disable = (Disable) obj;
		}

		Scan scan = null;
		Set<String> ips1 = new HashSet<String>();// 一个小时IP量
		Set<String> ips2 = new HashSet<String>();// 起始日到当前时间IP量
		// 推荐链接uv量查询，根据url,渠道id
		scan = new Scan();
		scan.setTimeRange(t1, t2);
		FilterList filterList1 = new FilterList(
				FilterList.Operator.MUST_PASS_ALL);

		RegexStringComparator comp = null;
		if (extension != null) {
			comp = new RegexStringComparator("^"
					+ extension.getExtension_link() + ".*");
		} else {
			comp = new RegexStringComparator("^" + disable.getDisable_link()
					+ ".*");
		}

		filterList1.addFilter(new SingleColumnValueExcludeFilter("info"
				.getBytes(), "j_url".getBytes(), CompareOp.EQUAL, comp));

		scan.setFilter(filterList1);

		List<Map<String, String>> list1 = getUVDtails(scan);

		for (int j = 0; j < list1.size(); j++) {
			String ip = list1.get(j).get("ip");
			if (ip != null && !"".equals(ip)) {
				ips1.add(ip);
			}
		}

		Long t3 = getStartTime(t1);

		Date date = new Date(t2);
		date.setHours(date.getHours() - 1);
		scan.setTimeRange(t3, date.getTime());

		List<Map<String, String>> list2 = getUVDtails(scan);
		for (int j = 0; j < list2.size(); j++) {
			ips2.add(list2.get(j).get("ip"));
			String ip = list2.get(j).get("ip");
			if (ip != null && !"".equals(ip)) {
				ips2.add(ip);
			}
		}

		Iterator<String> iterator1 = ips1.iterator();

		int a = 0;
		while (iterator1.hasNext()) {
			String ip1 = iterator1.next();
			if (ips2.contains(ip1)) {
				a++;
			}
		}
		return ips1.size() - a;
	}

	private List<Map<String, String>> getUVDtails(Scan scan) {
		final List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
		htemplate.find("dmp_uv", scan, new ResultsExtractor<Boolean>() {
			public Boolean extractData(ResultScanner rs) throws Exception {
				for (Result result : rs) {
					Map<String, String> map1 = new HashMap<String, String>();
					for (KeyValue kv : result.raw()) {
						map1.put(new String(kv.getQualifier()),
								new String(kv.getValue()));
					}
					list1.add(map1);
				}
				return true;
			}
		});
		return list1;
	}

	private Double getAverageTime(String sessionID, String isVisit,
			String ctime1, Long start, Long end) {
		try {
			Scan scan2 = new Scan();
			scan2.setTimeRange(start, end);
			FilterList filterList2 = new FilterList(
					FilterList.Operator.MUST_PASS_ALL);
			SingleColumnValueExcludeFilter sf2 = new SingleColumnValueExcludeFilter(
					"user".getBytes(), "sessionID".getBytes(), CompareOp.EQUAL,
					sessionID.getBytes());
			SingleColumnValueExcludeFilter sf3 = new SingleColumnValueExcludeFilter(
					"user".getBytes(), "isVisit".getBytes(), CompareOp.EQUAL,
					isVisit.getBytes());

			sf2.setFilterIfMissing(true);
			sf3.setFilterIfMissing(true);

			filterList2.addFilter(sf2);
			filterList2.addFilter(sf3);

			scan2.setFilter(filterList2);

			String ctime2 = htemplate.find("dmp_pv", scan2,
					new ResultsExtractor<String>() {
						public String extractData(ResultScanner rs)
								throws Exception {
							for (Result result : rs) {
								for (KeyValue kv : result.raw()) {
									if ("ctime".equals(new String(kv
											.getQualifier()))) {
										return new String(kv.getValue());
									}
								}
							}
							return null;
						}
					});

			if (ctime2 == null) {
				return 0.0;
			} else {
				return (double) (Long.parseLong(ctime2) - Long
						.parseLong(ctime1)) / 1000.0d;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0.0;
	}

	// 当日 00:00:00
	private static long getStartTime(Long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime().getTime();
	}
	// 当日小时时间
	private static long getHourTime(Long time, Integer Hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, Hour);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime().getTime();
	}
	// 当日23:59:59
	private static long getEndTime(Long time){
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 23); 
		cal.set(Calendar.SECOND, 59); 
		cal.set(Calendar.MINUTE, 59); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTime().getTime();
	}
	
	public static void main(String[] args) {
		
		
		long a=System.currentTimeMillis()-1000*60*60*24;
		Date d1=new Date(1498732543000l);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(d1));
//		System.out.println(a/1000);
//		System.out.println(getStartTime(a)/1000);
//		System.out.println(getEndTime(a)/1000);
//		1498664694
	}
	/**
	 * 1498639077 1498639077 1498639116 1498639116 1498639117 1498639117
	 */
}
