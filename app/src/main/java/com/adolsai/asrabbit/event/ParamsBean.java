package com.adolsai.asrabbit.event;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * <p>参数包装类，用于参数传递（效率高，用于Intent数据传递，或者用在进程间通信(IPC)，消息体）</p>
 *
 * @author zmingchun
 * @version 1.0 (2015/11/17)
 */
public class ParamsBean implements Parcelable {
    /********************************************************************/
            /**特别提示:新增变量，必须在writeToParcel和CREATOR补充**/
    /********************************************************************/
    /**来源类型*/
    public int sourceType;
    /**处理类型*/
    public String dealType;
    /**处理结果:0失败，1成功——用于BUS参数传递*/
    public int handleResult;
    /**处理结果信息*/
    public String handleResultMsg;

    /**登录用户sessionId*/
    public String sid;
    /**手机号码*/
    public String phone;
    /**验证码Id*/
    public String codeId;
    /**对象Id*/
    public String objectId;
    /**对象内容*/
    public String objectContent;
    /**对象时间*/
    public String objectTime;
    /**bundle对象，用来传递自定义的数据*/
    public Bundle bundle;
    /**
     * 由使用者定义这个标记位的含义
     */
    public boolean extraFlag;

    /**
     * 默认构造器
     */
    public ParamsBean(){

    }
    /**
     * 构造器
     * @param sourceType 处理类型
     */
    public ParamsBean(int sourceType){
        this.sourceType = sourceType;
    }
    // 必须实现方法
    //===========================================
    /**
     * 内容接口描述,默认返回0即可
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 写入接口函数，打包 ——该方法将类的数据写入外部提供的Parcel中.即打包需要传递的数据到Parcel容器保存，以便从parcel容器获取数据
     * @param dest dest
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        dest.writeInt(sourceType);
        dest.writeString(dealType);
        dest.writeInt(handleResult);
        dest.writeString(handleResultMsg);
        dest.writeString(sid);
        dest.writeString(phone);
        dest.writeString(codeId);
        dest.writeString(objectId);
        dest.writeString(objectContent);
        dest.writeString(objectTime);
        dest.writeBundle(bundle);
    }

    /**
     * 1.必须实现Parcelable.Creator接口,否则在获取ParamsBean数据的时候，会报错，如下：
     * android.os.BadParcelableException:
     * Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
     * 2.这个接口实现了从Percel容器读取ParamsBean数据，并返回ParamsBean对象给逻辑层使用
     * 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
     * 4.在读取Parcel容器里的数据时，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
     * 5.反序列化对象
     */
    public static final Creator CREATOR = new Creator(){
        @Override
        public ParamsBean createFromParcel(Parcel source) {
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ParamsBean p = new ParamsBean();
            p.setSourceType(source.readInt());
            p.setDealType(source.readString());
            p.setHandleResult(source.readInt());
            p.setHandleResultMsg(source.readString());
            p.setSid(source.readString());
            p.setPhone(source.readString());
            p.setCodeId(source.readString());
            p.setObjectId(source.readString());
            p.setObjectContent(source.readString());
            p.setObjectTime(source.readString());
            p.setBundle(source.readBundle());
            return p;
        }

        @Override
        public ParamsBean[] newArray(int size) {
            return new ParamsBean[size];
        }
    };

    @Override
    public String toString() {
        return "ParamsBean{" +
                "sourceType=" + sourceType +
                ", dealType='" + dealType + '\'' +
                ", handleResult=" + handleResult +
                ", handleResultMsg='" + handleResultMsg + '\'' +
                ", sid='" + sid + '\'' +
                ", phone='" + phone + '\'' +
                ", codeId='" + codeId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", objectContent='" + objectContent + '\'' +
                ", objectTime='" + objectTime + '\'' +
                '}';
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public int getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(int handleResult) {
        this.handleResult = handleResult;
    }

    public String getHandleResultMsg() {
        return handleResultMsg;
    }

    public void setHandleResultMsg(String handleResultMsg) {
        this.handleResultMsg = handleResultMsg;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectContent() {
        return objectContent;
    }

    public void setObjectContent(String objectContent) {
        this.objectContent = objectContent;
    }

    public String getObjectTime() {
        return objectTime;
    }

    public void setObjectTime(String objectTime) {
        this.objectTime = objectTime;
    }

    public String getDealType() {
        return null==dealType?"":dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}