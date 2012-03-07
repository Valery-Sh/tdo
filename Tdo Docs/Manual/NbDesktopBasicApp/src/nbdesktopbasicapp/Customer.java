/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nbdesktopbasicapp;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author valery
 */
@Entity
@Table(name = "CUSTOMER", catalog = "", schema = "APP")
@NamedQueries({@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"), @NamedQuery(name = "Customer.findByCustomerId", query = "SELECT c FROM Customer c WHERE c.customerId = :customerId"), @NamedQuery(name = "Customer.findByDiscountCode", query = "SELECT c FROM Customer c WHERE c.discountCode = :discountCode"), @NamedQuery(name = "Customer.findByZip", query = "SELECT c FROM Customer c WHERE c.zip = :zip"), @NamedQuery(name = "Customer.findByName", query = "SELECT c FROM Customer c WHERE c.name = :name"), @NamedQuery(name = "Customer.findByAddressline1", query = "SELECT c FROM Customer c WHERE c.addressline1 = :addressline1"), @NamedQuery(name = "Customer.findByAddressline2", query = "SELECT c FROM Customer c WHERE c.addressline2 = :addressline2"), @NamedQuery(name = "Customer.findByCity", query = "SELECT c FROM Customer c WHERE c.city = :city"), @NamedQuery(name = "Customer.findByState", query = "SELECT c FROM Customer c WHERE c.state = :state"), @NamedQuery(name = "Customer.findByPhone", query = "SELECT c FROM Customer c WHERE c.phone = :phone"), @NamedQuery(name = "Customer.findByFax", query = "SELECT c FROM Customer c WHERE c.fax = :fax"), @NamedQuery(name = "Customer.findByEmail", query = "SELECT c FROM Customer c WHERE c.email = :email"), @NamedQuery(name = "Customer.findByCreditLimit", query = "SELECT c FROM Customer c WHERE c.creditLimit = :creditLimit")})
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    @Basic(optional = false)
    @Column(name = "DISCOUNT_CODE")
    private char discountCode;
    @Basic(optional = false)
    @Column(name = "ZIP")
    private String zip;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ADDRESSLINE1")
    private String addressline1;
    @Column(name = "ADDRESSLINE2")
    private String addressline2;
    @Column(name = "CITY")
    private String city;
    @Column(name = "STATE")
    private String state;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CREDIT_LIMIT")
    private Integer creditLimit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private List<PurchaseOrder> purchaseOrderCollection;

    public Customer() {
    }

    public Customer(Integer customerId) {
        this.customerId = customerId;
    }

    public Customer(Integer customerId, char discountCode, String zip) {
        this.customerId = customerId;
        this.discountCode = discountCode;
        this.zip = zip;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public char getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(char discountCode) {
        this.discountCode = discountCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    public List<PurchaseOrder> getPurchaseOrderCollection() {
        return purchaseOrderCollection;
    }

    public void setPurchaseOrderCollection(List<PurchaseOrder> purchaseOrderCollection) {
        this.purchaseOrderCollection = purchaseOrderCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nbdesktopbasicapp.Customer[customerId=" + customerId + "]";
    }


}
