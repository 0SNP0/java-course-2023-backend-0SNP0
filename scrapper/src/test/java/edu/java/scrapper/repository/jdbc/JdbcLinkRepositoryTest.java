package edu.java.scrapper.repository.jdbc;

import edu.java.scrapper.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.LongStream;

public class JdbcLinkRepositoryTest extends JdbcRepositoryTest<JdbcLinkRepository, Link> {
    private static final List<Link> testEntities = LongStream.range(1, 3)
        .mapToObj(x -> new Link().setLinkId(x)
            .setUrl(URI.create("http://u.r.l/%d".formatted(x)))
            .setUpdatedAt(OffsetDateTime.now())
        ).toList();

    protected JdbcLinkRepositoryTest() {
        super(JdbcLinkRepository::new, testEntities);
    }


}
