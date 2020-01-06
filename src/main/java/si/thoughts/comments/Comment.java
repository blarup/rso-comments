package si.thoughts.comments;

import java.time.Instant;

public class Comment {
    private Integer id;
    private Integer textId;
    private String content;
    private Instant created;

    public Integer getId(){return id;}

    public void setId(Integer id) {this.id = id;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getTextId() {
        return textId;
    }

    public void setTextId(Integer textId) {
        this.textId = textId;
    }
}
