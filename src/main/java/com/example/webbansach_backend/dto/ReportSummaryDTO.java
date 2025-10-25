package com.example.webbansach_backend.dto;

import java.util.List;
import java.util.Map;

public class ReportSummaryDTO {
    private Double revenue;
    private Long orders;
    private Map<String, Long> byStatus;
    private List<SeriesPoint> revenueSeries;
    private List<TopItem> topBooks;

    public Double getRevenue() { return revenue; }
    public void setRevenue(Double revenue) { this.revenue = revenue; }
    public Long getOrders() { return orders; }
    public void setOrders(Long orders) { this.orders = orders; }
    public Map<String, Long> getByStatus() { return byStatus; }
    public void setByStatus(Map<String, Long> byStatus) { this.byStatus = byStatus; }
    public List<SeriesPoint> getRevenueSeries() { return revenueSeries; }
    public void setRevenueSeries(List<SeriesPoint> revenueSeries) { this.revenueSeries = revenueSeries; }
    public List<TopItem> getTopBooks() { return topBooks; }
    public void setTopBooks(List<TopItem> topBooks) { this.topBooks = topBooks; }

    public static class SeriesPoint {
        private String date;
        private Double value;
        public SeriesPoint() {}
        public SeriesPoint(String date, Double value) { this.date = date; this.value = value; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
    }
    public static class TopItem {
        private String name;
        private Long quantity;
        public TopItem() {}
        public TopItem(String name, Long quantity) { this.name = name; this.quantity = quantity; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getQuantity() { return quantity; }
        public void setQuantity(Long quantity) { this.quantity = quantity; }
    }
}
