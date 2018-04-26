package cn.jyl.model;

import com.sinosoft.microservice.common.BaseModel;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Arrays;

@Entity
public class Message extends BaseModel<String> {
    private static final long serialVersionUID = -7100684289520954882L;
    private String id;
    private String messageid;
    private String messagebody;
    private Timestamp edittime;
    private Integer resendcount;
    private boolean isdead;
    private String remark;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "messageid")
    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    @Basic
    @Column(name = "messagebody")
    public String getMessagebody() {
        return messagebody;
    }

    public void setMessagebody(String messagebody) {
        this.messagebody = messagebody;
    }

    @Basic
    @Column(name = "edittime")
    public Timestamp getEdittime() {
        return edittime;
    }

    public void setEdittime(Timestamp edittime) {
        this.edittime = edittime;
    }

    @Basic
    @Column(name = "resendcount")
    public Integer getResendcount() {
        return resendcount;
    }

    public void setResendcount(Integer resendcount) {
        this.resendcount = resendcount;
    }

    @Basic
    @Column(name = "isdead")
    public boolean getIsdead() {
        return isdead;
    }

    public void setIsdead(boolean isdead) {
        this.isdead = isdead;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (messageid != null ? !messageid.equals(message.messageid) : message.messageid != null) return false;
        if (messagebody != null ? !messagebody.equals(message.messagebody) : message.messagebody != null) return false;
        if (edittime != null ? !edittime.equals(message.edittime) : message.edittime != null) return false;
        if (resendcount != null ? !resendcount.equals(message.resendcount) : message.resendcount != null) return false;
        if (remark != null ? !remark.equals(message.remark) : message.remark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (messageid != null ? messageid.hashCode() : 0);
        result = 31 * result + (messagebody != null ? messagebody.hashCode() : 0);
        result = 31 * result + (edittime != null ? edittime.hashCode() : 0);
        result = 31 * result + (resendcount != null ? resendcount.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }
}
