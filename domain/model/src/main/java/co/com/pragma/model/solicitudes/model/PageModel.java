package co.com.pragma.model.solicitudes.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PageModel {
    private long totalElements;
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public PageModel(long totalElements, int currentPage, int size) {

        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
    }

}
