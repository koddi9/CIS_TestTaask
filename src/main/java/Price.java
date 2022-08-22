
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

public class Price {
    private long id;
    private String product_code;
    private int number;
    private int depart;
    private Date begin;
    private Date end;
    private long value;

    public Price() {

    }

    public Price(long id, String product_code, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.product_code = product_code;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public Price(Price price) {
        this.id = price.getId();
        this.product_code = price.getProduct_code();
        this.number = price.getNumber();
        this.depart = price.getDepart();
        this.begin = price.getBegin();
        this.end = price.getEnd();
        this.value = price.getValue();
    }

    public long getId() {
        return id;
    }

    public Price setId(long id) {
        this.id = id;
        return this;
    }

    public String getProduct_code() {
        return product_code;
    }

    public Price setProduct_code(String product_code) {
        this.product_code = product_code;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public Price setNumber(int number) {
        this.number = number;
        return this;
    }

    public int getDepart() {
        return depart;
    }

    public Price setDepart(int depart) {
        this.depart = depart;
        return this;
    }

    public Date getBegin() {
        return begin;
    }

    public Price setBegin(Date begin) {
        this.begin = begin;
        return this;
    }

    public Date getEnd() {
        return end;
    }

    public Price setEnd(Date end) {
        this.end = end;
        return this;
    }

    public long getValue() {
        return value;
    }

    public Price setValue(long value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return number == price.number &&
                depart == price.depart &&
                Objects.equals(product_code, price.product_code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_code, number, depart);
    }

    @Override
    public String toString() {
        StringJoiner message = new StringJoiner(", ", getClass().getSimpleName() + "[", "]");
        message.add("id=" + id)
                .add("product_code='" + product_code + "'")
                .add("number=" + number)
                .add("depart=" + depart)
                .add("begin=" + Optional.ofNullable(begin).map(Date::toInstant))
                .add("end=" + Optional.ofNullable(end).map(Date::toInstant))
                .add("value=" + value);
        return message.toString();
    }
}

