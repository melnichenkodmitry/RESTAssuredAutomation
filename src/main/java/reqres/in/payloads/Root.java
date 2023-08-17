package reqres.in.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * Вспомогательный класс для парсинга всех значений JSON формата.<br>
 * В тестах не применяется
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Root {
    public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public ArrayList<Datum> data;
    public Support support;
}
