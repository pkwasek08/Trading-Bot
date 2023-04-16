package pl.project.trade;

import pl.project.bot.BotsEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "trades", schema = "public", catalog = "bot")
public class TradesEntity {
    private int id;
    private Timestamp date;
    private BigDecimal balance;
    private String pairStock;
    private String type;
    private BotsEntity botsByBotId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Basic
    @Column(name = "balance", nullable = true, precision = 2)
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "pair_stock", nullable = true, length = -1)
    public String getPairStock() {
        return pairStock;
    }

    public void setPairStock(String pairStock) {
        this.pairStock = pairStock;
    }

    @Basic
    @Column(name = "type", nullable = true, length = -1)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradesEntity that = (TradesEntity) o;

        if (id != that.id) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (balance != null ? !balance.equals(that.balance) : that.balance != null) return false;
        if (pairStock != null ? !pairStock.equals(that.pairStock) : that.pairStock != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (pairStock != null ? pairStock.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "bot_id", referencedColumnName = "id")
    public BotsEntity getBotsByBotId() {
        return botsByBotId;
    }

    public void setBotsByBotId(BotsEntity botsByBotId) {
        this.botsByBotId = botsByBotId;
    }
}
