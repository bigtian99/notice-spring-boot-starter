package club.bigtian.notice.domain.dto;

import lombok.Data;

/**
 * @author bigtian
 */
@Data
public class PageListDto {

    private int page;
    private int limit;
    private String handled;
}
