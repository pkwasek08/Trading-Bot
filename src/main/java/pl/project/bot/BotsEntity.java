package pl.project.bot;

import pl.project.trade.TradesEntity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "bots", schema = "public", catalog = "bot")
public class BotsEntity {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal resultValue;
    private BigDecimal budget;
    private String status;
    private String strategy;
    private String parameters;
    private LocalDateTime createDate;
    private String pairStock;
    private String resampleFreq;
    private BigDecimal roi;
    private BigDecimal wlRatio;
    private Collection<TradesEntity> tradesById;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "start_date")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "end_date")
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "result_value", precision = 2)
    public BigDecimal getResultValue() {
        return resultValue;
    }

    public void setResultValue(BigDecimal resultValue) {
        this.resultValue = resultValue;
    }

    @Basic
    @Column(name = "budget", precision = 2)
    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @Basic
    @Column(name = "status", length = -1)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "strategy", length = -1)
    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    @Basic
    @Column(name = "parameters", length = -1)
    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Basic
    @Column(name = "create_date")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "pair_stock", length = -1)
    public String getPairStock() {
        return pairStock;
    }

    public void setPairStock(String pairStock) {
        this.pairStock = pairStock;
    }


    @Basic
    @Column(name = "resample_frequency", length = -1)
    public String getResampleFreq() {
        return resampleFreq;
    }

    public void setResampleFreq(String resampleFreq) {
        this.resampleFreq = resampleFreq;
    }

    @Basic
    @Column(name = "roi", precision = 2)
    public BigDecimal getRoi() {
        return roi;
    }

    public void setRoi(BigDecimal ROI) {
        this.roi = ROI;
    }

    @Basic
    @Column(name = "wlratio", precision = 2)
    public BigDecimal getWlRatio() {
        return wlRatio;
    }

    public void setWlRatio(BigDecimal WLRatio) {
        this.wlRatio = WLRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotsEntity that = (BotsEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(startDate, that.startDate)) return false;
        if (!Objects.equals(endDate, that.endDate)) return false;
        if (!Objects.equals(resultValue, that.resultValue)) return false;
        if (!Objects.equals(budget, that.budget)) return false;
        if (!Objects.equals(status, that.status)) return false;
        if (!Objects.equals(strategy, that.strategy)) return false;
        if (!Objects.equals(parameters, that.parameters)) return false;
        if (!Objects.equals(createDate, that.createDate)) return false;
        if (!Objects.equals(pairStock, that.pairStock)) return false;
        if (!Objects.equals(resampleFreq, that.resampleFreq)) return false;
        if (!Objects.equals(roi, that.roi)) return false;
        if (!Objects.equals(wlRatio, that.wlRatio)) return false;
        return Objects.equals(tradesById, that.tradesById);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (resultValue != null ? resultValue.hashCode() : 0);
        result = 31 * result + (budget != null ? budget.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (strategy != null ? strategy.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (pairStock != null ? pairStock.hashCode() : 0);
        result = 31 * result + (resampleFreq != null ? resampleFreq.hashCode() : 0);
        result = 31 * result + (tradesById != null ? tradesById.hashCode() : 0);
        result = 31 * result + (roi != null ? roi.hashCode() : 0);
        result = 31 * result + (wlRatio != null ? wlRatio.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "bot", cascade = CascadeType.ALL)
    public Collection<TradesEntity> getTradesById() {
        return tradesById;
    }

    public void setTradesById(Collection<TradesEntity> tradesById) {
        this.tradesById = tradesById;
    }
}
