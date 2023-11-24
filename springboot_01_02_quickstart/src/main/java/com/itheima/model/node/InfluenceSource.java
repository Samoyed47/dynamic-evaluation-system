package com.itheima.model.node;

import com.itheima.model.relationship.Influent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@Data
@NoArgsConstructor
@Node("influenceSource")
public class InfluenceSource {
    @Id
    @GeneratedValue
    private Long Id;

    @Property("timestamp")
    private Integer timestamp;

    @Relationship(type="influent",direction = Relationship.Direction.INCOMING)
    private List<Influent> userInfluence;

}
