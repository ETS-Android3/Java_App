package uk.ac.abertay.cmp400.java_app;

public class DisplayModel {

    private String title;
    private String description;
    private Boolean isTitle;
    private int img;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public DisplayModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsTitle() {
        return isTitle;
    }

    public void setIsTitle(Boolean isTitle) {
        this.isTitle = isTitle;
    }
}
