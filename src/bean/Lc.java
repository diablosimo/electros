package bean;

public class Lc{
    private String reference;
    private Integer quantite;
    private Double prix;

    public Lc(String reference, Integer quantite, Double prix) {
        this.reference = reference;
        this.quantite = quantite;
        this.prix = prix;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Lc{" +
                "reference='" + reference + '\'' +
                ", quantite=" + quantite +
                ", prix=" + prix +
                '}';
    }
}