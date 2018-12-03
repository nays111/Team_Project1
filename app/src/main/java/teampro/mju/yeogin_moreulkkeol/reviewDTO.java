package teampro.mju.yeogin_moreulkkeol;

public class reviewDTO {
        public String id;
        public String comment;
        public float rating;

    public void setId(String id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }
}
