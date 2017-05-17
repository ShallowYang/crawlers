package com.baza.crawler.util;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/5/16
 * Time: 17:44
 * Description:
 */
public class Tools {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String datakeys = "li,wang,zhang,liu,chen,yang,zhao,huang,zhou,wu,xu,sun,hu,zhu,gao,lin,he,guo,ma,luo,liang,song,zheng,xie,han,tang,feng,yu,dong,xiao,cheng,cao,yuan,deng,xu,fu,shen,zeng,peng,lv,su,lu,jiang,cai,jia,ding,wei,xue,ye,yan,yu,pan,du,dai,xia,zhong,wang,tian,ren,jiang,fan,fang,shi,yao,tan,liao,zhou,xiong,jin,lu,hao,kong,bai,cui,kang,mao,qiu,qin,jiang,shi,gu,hou,shao,meng,long,wan,duan,lei,qian,tang,yin,li,yi,chang,wu,qiao,he,lai,gong,wen,pang,fan,lan,yin,shi,tao,hong,zhai,an,yan,ni,yan,niu,wen,lu,ji,yu,zhang,lu,ge,wu,wei,shen,you,bi,nie,cong,jiao,xiang,liu,xing,lu,yue,qi,yan,mei,mo,zhuang,xin,guan,zhu,zuo,tu,gu,qi,shi,shu,geng,mou,bo,lu,zhan,guan,miao,ling,fei,ji,le,sheng,tong,ou,zhen,xiang,qu,cheng,you,yang,pei,xi,wei,cha,qu,bao,wei,tan,huo,weng,sui,zhi,gan,jing,bo,dan,bao,si,bai,ning,ke,ruan,gui,min,ouyang,jie,qiang,chai,hua,che,ran,fang,bian";
        List<String> strings = Arrays.asList(datakeys.split(","));
        for(String item:strings){

            System.out.println(item);

        }
    }

}
