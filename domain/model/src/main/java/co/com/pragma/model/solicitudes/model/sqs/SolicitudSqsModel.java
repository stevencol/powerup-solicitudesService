package co.com.pragma.model.solicitudes.model.sqs;


import java.math.BigDecimal;

public class SolicitudSqsModel {
    Long id;
    UserSQSModel userDto;
    BigDecimal mount;
    Integer termInMonths;
    String loanType;
    String status;
    double interestRate;

    public SolicitudSqsModel(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public SolicitudSqsModel() {
    }

    public SolicitudSqsModel(Long id, UserSQSModel userDto, BigDecimal mount, Integer termInMonths, String loanType, String status, double interestRate) {
        this.id = id;
        this.userDto = userDto;
        this.mount = mount;
        this.termInMonths = termInMonths;
        this.loanType = loanType;
        this.status = status;
        this.interestRate = interestRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserSQSModel getUserDto() {
        return userDto;
    }

    public void setUserDto(UserSQSModel userDto) {
        this.userDto = userDto;
    }

    public BigDecimal getMount() {
        return mount;
    }

    public void setMount(BigDecimal mount) {
        this.mount = mount;
    }

    public Integer getTermInMonths() {
        return termInMonths;
    }

    public void setTermInMonths(Integer termInMonths) {
        this.termInMonths = termInMonths;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
