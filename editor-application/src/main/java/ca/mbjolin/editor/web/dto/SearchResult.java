package ca.mbjolin.editor.web.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

  private Integer total_count = Integer.valueOf(3);

  private Boolean incomplete_results = false;

  private List<Item> items = new ArrayList<>();

  public List<Item> getItems() {
    return items;

  }

  public void setItems(List<Item> items) {
    this.items = items;

  }

  public Integer getTotal_count() {
    return total_count;

  }

  public void setTotal_count(Integer total_count) {
    this.total_count = total_count;

  }

  public Boolean getIncomplete_results() {
    return incomplete_results;

  }

  public void setIncomplete_results(Boolean incomplete_results) {
    this.incomplete_results = incomplete_results;

  }

}
