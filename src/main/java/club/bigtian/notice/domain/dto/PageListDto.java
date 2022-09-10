package club.bigtian.notice.domain.dto;

import lombok.Data;

/**
 * @author bigtian
 * @Description: 页面列表
 * @date 2022/9/817:34
 */
@Data
public class PageListDto {

    private int page;
    private int limit;
    private String handled;
}
