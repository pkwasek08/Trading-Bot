package pl.project.bot;

import pl.project.trade.TradesEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "bots", schema = "public", catalog = "bot")
public class BotsEntity {
    private int id;
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
    private Collection<TradesEntity> tradesById;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BotsEntity that = (BotsEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (resultValue != null ? !resultValue.equals(that.resultValue) : that.resultValue != null) return false;
        if (budget != null ? !budget.equals(that.budget) : that.budget != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (strategy != null ? !strategy.equals(that.strategy) : that.strategy != null) return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (pairStock != null ? !pairStock.equals(that.pairStock) : that.pairStock != null) return false;
        if (resampleFreq != null ? !resampleFreq.equals(that.resampleFreq) : that.resampleFreq != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
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
        return result;
    }

    @OneToMany(mappedBy = "bot")
    public Collection<TradesEntity> getTradesById() {
        return tradesById;
    }

    public void setTradesById(Collection<TradesEntity> tradesById) {
        this.tradesById = tradesById;
    }
}
