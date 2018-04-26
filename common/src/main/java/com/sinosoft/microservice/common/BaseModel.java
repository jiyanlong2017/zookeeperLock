package com.sinosoft.microservice.common;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/31.
 */
public abstract class BaseModel<ID> implements Serializable{

    public abstract void setId(final ID id);

    public abstract  ID getId();
}
