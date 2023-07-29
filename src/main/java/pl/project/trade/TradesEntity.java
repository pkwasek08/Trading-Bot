package pl.project.trade;

import pl.project.bot.BotsEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "trades", schema = "public", catalog = "bot")
public class TradesEntity {
    private int id;
    private String type;
    private String pairStock;
    private BigDecimal openPrice;
    private Timestamp dateOpen;
    private Timestamp dateClose;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private String comment;
    private BotsEntity bot;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type", length = -1)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    @Column(name = "open_price", precision = 2)
    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    @Basic
    @Column(name = "date_open")
    public Timestamp getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(Timestamp dateOpen) {
        this.dateOpen = dateOpen;
    }

    @Basic
    @Column(name = "date_close")
    public Timestamp getDateClose() {
        return dateClose;
    }

    public void setDateClose(Timestamp dateClose) {
        this.dateClose = dateClose;
    }

    @Basic
    @Column(name = "balance_before", precision = 2)
    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    @Basic
    @Column(name = "balance_after", precision = 2)
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @Basic
    @Column(name = "stop_loss", precision = 2)
    public BigDecimal getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(BigDecimal stopLoss) {
        this.stopLoss = stopLoss;
    }

    @Basic
    @Column(name = "take_profit", precision = 2)
    public BigDecimal getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(BigDecimal takeProfit) {
        this.takeProfit = takeProfit;
    }

    @Basic
    @Column(name = "comment", length = -1)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradesEntity that = (TradesEntity) o;

        if (id != that.id) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (pairStock != null ? !pairStock.equals(that.pairStock) : that.pairStock != null) return false;
        if (openPrice != null ? !openPrice.equals(that.openPrice) : that.openPrice != null) return false;
        if (dateOpen != null ? !dateOpen.equals(that.dateOpen) : that.dateOpen != null) return false;
        if (dateClose != null ? !dateClose.equals(that.dateClose) : that.dateClose != null) return false;
        if (balanceBefore != null ? !balanceBefore.equals(that.balanceBefore) : that.balanceBefore != null)
            return false;
        if (balanceAfter != null ? !balanceAfter.equals(that.balanceAfter) : that.balanceAfter != null) return false;
        if (stopLoss != null ? !stopLoss.equals(that.stopLoss) : that.stopLoss != null) return false;
        if (takeProfit != null ? !takeProfit.equals(that.takeProfit) : that.takeProfit != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (pairStock != null ? pairStock.hashCode() : 0);
        result = 31 * result + (openPrice != null ? openPrice.hashCode() : 0);
        result = 31 * result + (dateOpen != null ? dateOpen.hashCode() : 0);
        result = 31 * result + (dateClose != null ? dateClose.hashCode() : 0);
        result = 31 * result + (balanceBefore != null ? balanceBefore.hashCode() : 0);
        result = 31 * result + (balanceAfter != null ? balanceAfter.hashCode() : 0);
        result = 31 * result + (stopLoss != null ? stopLoss.hashCode() : 0);
        result = 31 * result + (takeProfit != null ? takeProfit.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "bot_id", referencedColumnName = "id")
    public BotsEntity getBot() {
        return bot;
    }

    public void setBot(BotsEntity botsByBotId) {
        this.bot = botsByBotId;
    }
}
