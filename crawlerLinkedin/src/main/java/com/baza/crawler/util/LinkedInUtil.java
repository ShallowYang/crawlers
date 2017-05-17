package com.baza.crawler.util;

import com.baza.crawler.domain.LinkedinModel;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/15
 * Time: 16:38
 * Description:
 */
public class LinkedInUtil {

    public static LinkedinModel parseInPage(String content){
        Document parse = Jsoup.parse(content);
        Elements uniqueElements = parse.select("code");
        JSONArray experience = new JSONArray();
        JSONArray educations = new JSONArray();
        JSONArray schools = new JSONArray();
        JSONArray skills = new JSONArray();
        JSONObject selfInfo = new JSONObject();

        JSONObject commonDate = new JSONObject();
        JSONObject rangeDate = new JSONObject();

        for (Element uniqueItem : uniqueElements) {
            String itemTxt = uniqueItem.text();
            if (!itemTxt.contains("request")) {
                if (itemTxt.startsWith("{")) {
                    JSONArray jsonArray = JSONObject.fromObject(itemTxt).getJSONArray("included");
                    if (jsonArray.size() > 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            try {
                                JSONObject jobject = JSONObject.fromObject(jsonArray.get(i));
                                if (jobject.containsKey("$type")) {
                                    String typeStr = jobject.getString("$type");
                                    if (typeStr.equals("com.linkedin.common.Date")) {
                                        commonDate.accumulate(jobject.getString("$id"), jobject);
                                    } else if (typeStr.equals("com.linkedin.voyager.common.DateRange")) {
                                        rangeDate.accumulate(jobject.getString("$id"), jobject);
                                    } else if (typeStr.equals("com.linkedin.voyager.identity.shared.MiniProfile")) {
                                        // 他人信息摘要（包括自己）
                                        String publicIdentifier = jobject.getString("publicIdentifier");
                                    } else if (typeStr.equals("com.linkedin.voyager.identity.profile.Skill")) {
                                        // 个人技能
                                        skills.add(jobject.getString("name"));
                                    } else if (typeStr.equals("com.linkedin.voyager.identity.profile.Profile")) {
                                        // 个人信息
                                        jobject.remove("supportedLocales");
                                        jobject.remove("versionTag");
                                        jobject.remove("pictureInfo");
                                        jobject.remove("industryUrn");
                                        jobject.remove("$type");
                                        jobject.remove("defaultLocale");
                                        jobject.remove("$deletedFields");
                                        jobject.remove("entityUrn");
                                        jobject.remove("location");
                                        jobject.remove("miniProfile");
                                        jobject.remove("backgroundImage");
                                        jobject.remove("state");
                                        selfInfo = jobject;
                                    } else if (typeStr.equals("com.linkedin.voyager.entities.shared.MiniSchool")) {
                                        // 学校
                                        jobject.remove("$deletedFields");
                                        jobject.remove("objectUrn");
                                        jobject.remove("entityUrn");
                                        jobject.remove("trackingId");
                                        jobject.remove("$type");
                                        jobject.remove("logo");
                                        schools.add(jobject);
                                    } else if (typeStr.equals("com.linkedin.voyager.identity.profile.Education")) {
                                        // 教育
                                        jobject.remove("$deletedFields");
                                        jobject.remove("entityUrn");
                                        jobject.remove("schoolUrn");
                                        jobject.remove("$type");
                                        jobject.remove("courses");
                                        String dateStr = buildRangeDate(commonDate, rangeDate, jobject);
                                        jobject.accumulate("rangeDate", dateStr);
                                        jobject.remove("degreeUrn");
                                        jobject.remove("timePeriod");
                                        educations.add(jobject);
                                    } else if (typeStr.equals("com.linkedin.voyager.identity.profile.Position")) {
                                        // 公司
                                        jobject.remove("$deletedFields");
                                        jobject.remove("entityUrn");
                                        String dateStr = buildRangeDate(commonDate, rangeDate, jobject);
                                        jobject.accumulate("rangeDate", dateStr);
                                        jobject.remove("timePeriod");
                                        jobject.remove("company");
                                        jobject.remove("companyUrn");
                                        jobject.remove("$type");
                                        experience.add(jobject.toString());
                                    }
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
            }

        }
        LinkedinModel model = new LinkedinModel();
        try {

            model.setExperiences(experience.toString());
            model.setEducations(educations.toString());
            model.setEducations(schools.toString());
            model.setSkills(skills.toString());
            if (selfInfo.containsKey("firstName")) {
                model.setFirstName(selfInfo.getString("firstName"));
            }
            if (selfInfo.containsKey("lastName")) {
                model.setLastName(selfInfo.getString("lastName"));
            }
            if (selfInfo.containsKey("industryName")) {
                model.setIndustryName(selfInfo.getString("industryName"));
            }

            if (selfInfo.containsKey("headline")) {
                model.setHeadline(selfInfo.getString("headline"));
            }
            if (selfInfo.containsKey("locationName")) {
                model.setAddress(selfInfo.getString("locationName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 重建日期范围
     *
     * @param commonDate
     * @param rangeDate
     * @param elementObject
     * @return
     */
    public static String buildRangeDate(JSONObject commonDate, JSONObject rangeDate, JSONObject elementObject) {
        if (!elementObject.containsKey("timePeriod")) {
            return "";
        }
        String timeId = elementObject.getString("timePeriod");
        JSONObject jsonObject = rangeDate.getJSONObject(timeId);
        String start = "";
        String end = "";
        if (jsonObject.containsKey("startDate")) {
            start = jsonObject.getString("startDate");
        } else {
            return "";
        }
        JSONObject startDate = commonDate.getJSONObject(start);
        StringBuffer startStr = new StringBuffer(startDate.getString("year"));
        if (startDate.containsKey("month")) {
            startStr.append("-" + startDate.getString("month"));
        }
        if (startDate.containsKey("month") && startDate.containsKey("day")) {
            startStr.append("-" + startDate.getString("day"));
        }

        if (jsonObject.containsKey("endDate")) {
            end = jsonObject.getString("endDate");
        } else {
            return startStr.toString();
        }
        JSONObject endDate = commonDate.getJSONObject(end);
        StringBuffer endStr = new StringBuffer(endDate.getString("year"));

        if (endDate.containsKey("month")) {
            endStr.append("-" + endDate.getString("month"));
        }
        if (endDate.containsKey("month") && startDate.containsKey("day")) {
            endStr.append("-" + endDate.getString("day"));
        }
        return startStr.toString() + "至" + endStr.toString();
    }
}
