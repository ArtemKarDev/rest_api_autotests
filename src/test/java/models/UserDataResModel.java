package models;

import lombok.Data;
import java.util.Date;
@Data
public class UserDataResModel {
    String name, job, id;
    public Date createdAt;
}
