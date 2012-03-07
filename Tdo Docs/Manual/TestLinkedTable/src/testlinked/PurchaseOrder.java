/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testlinked;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author valery
 */
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer orderNum;
    private int productId;
    private Short quantity;
    private BigDecimal shippingCost;
    private Date salesDate;
    private Date shippingDate;
    private String freightCompany;
    private Customer customerId;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public PurchaseOrder(Integer orderNum, int productId) {
        this.orderNum = orderNum;
        this.productId = productId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Short getQuantity() {
        return quantity;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getFreightCompany() {
        return freightCompany;
    }

    public void setFreightCompany(String freightCompany) {
        this.freightCompany = freightCompany;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderNum != null ? orderNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder other = (PurchaseOrder) object;
        if ((this.orderNum == null && other.orderNum != null) || (this.orderNum != null && !this.orderNum.equals(other.orderNum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nbdesktopbasicapp.PurchaseOrder[orderNum=" + orderNum + "]";
    }


}
