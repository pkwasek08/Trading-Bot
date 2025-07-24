package pl.project.trade;

import pl.project.bot.BotsEntity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "trades", schema = "public", catalog = "bot")
public class TradesEntity {
    private Long id;
    private String type;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private LocalDateTime dateOpen;
    private LocalDateTime dateClose;
    private BigDecimal profitLose;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private String comment;
    private Long amount;
    private BotsEntity bot;

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
    @Column(name = "type", length = -1)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    @Column(name = "close_price", precision = 2)
    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    @Basic
    @Column(name = "date_open")
    public LocalDateTime getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(LocalDateTime dateOpen) {
        this.dateOpen = dateOpen;
    }

    @Basic
    @Column(name = "date_close")
    public LocalDateTime getDateClose() {
        return dateClose;
    }

    public void setDateClose(LocalDateTime dateClose) {
        this.dateClose = dateClose;
    }

    @Basic
    @Column(name = "profit_lose", precision = 2)
    public BigDecimal getProfitLose() {
        return profitLose;
    }

    public void setProfitLose(BigDecimal balanceBefore) {
        this.profitLose = balanceBefore;
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

    @Basic
    @Column(name = "amount")
            public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradesEntity that = (TradesEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(type, that.type)) return false;
        if (!Objects.equals(openPrice, that.openPrice)) return false;
        if (!Objects.equals(closePrice, that.closePrice)) return false;
        if (!Objects.equals(dateOpen, that.dateOpen)) return false;
        if (!Objects.equals(dateClose, that.dateClose)) return false;
        if (!Objects.equals(profitLose, that.profitLose)) return false;
        if (!Objects.equals(stopLoss, that.stopLoss)) return false;
        if (!Objects.equals(takeProfit, that.takeProfit)) return false;
        if (!Objects.equals(amount, that.amount)) return false;
        if (!Objects.equals(comment, that.comment)) return false;
        return Objects.equals(bot, that.bot);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (openPrice != null ? openPrice.hashCode() : 0);
        result = 31 * result + (closePrice != null ? closePrice.hashCode() : 0);
        result = 31 * result + (dateOpen != null ? dateOpen.hashCode() : 0);
        result = 31 * result + (dateClose != null ? dateClose.hashCode() : 0);
        result = 31 * result + (profitLose != null ? profitLose.hashCode() : 0);
        result = 31 * result + (stopLoss != null ? stopLoss.hashCode() : 0);
        result = 31 * result + (takeProfit != null ? takeProfit.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (bot != null ? bot.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "bot_id", referencedColumnName = "id", nullable = false)
    public BotsEntity getBot() {
        return bot;
    }

    public void setBot(BotsEntity botsByBotId) {
        this.bot = botsByBotId;
    }
}
