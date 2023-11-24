package com.itheima.model.node;

import com.itheima.model.relationship.Hot;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@Data
@NoArgsConstructor
@Node("hotSource")
public class HotSource {
    @Id
    @GeneratedValue
    private Long id;

    @Property("timestamp")
    private Integer timestamp;

    @Relationship(type="hot",direction = Relationship.Direction.INCOMING)
    private List<Hot> postHots;

    public List<Hot> getPostHots() {
        return postHots;
    }
}
