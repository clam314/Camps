package com.clam.camps.utils;

/**
 * Created by clam314 on 2016/3/2.
 */
public class Result {
    private String _id; //唯一标识
    private String desc;//资源说明
    private String who;//提供者
    private String publishedAt;//发布日期
    private String url;//资源链接
    private String type;//资源类型

    public String get_id(){
        return  _id;
    }
    public String getDesc(){
        return desc;
    }
    public String getWho(){
        return who;
    }
    public String getPublishedAt(){
        return publishedAt;
    }
    public String getUrl(){
        return url;
    }
    public String getType(){
        return type;
    }

    public void set_id(String _id){
        this._id = _id;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setWho(String who){
        this.who = who;
    }
    public void setPublishedAt(String publishedAt){
        this.publishedAt = publishedAt;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setType(String type){
        this.type = type;
    }
}
