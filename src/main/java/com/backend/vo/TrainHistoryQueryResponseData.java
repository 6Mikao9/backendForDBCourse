package com.backend.vo;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class TrainHistoryQueryResponseData {

    @JsonProperty("data")
    private List<TrainRecord> data;

    // 省略getter和setter方法
    public List<TrainRecord> getData() {
        return data;
    }

    public void setData(List<TrainRecord> data) {
        this.data = data;
    }

    // 内部静态类，用于表示单个训练记录
    public static class TrainRecord {
        @JsonProperty("train_id")
        private int trainId;

        @JsonProperty("startTime")
        private String startTime;

        @JsonProperty("endTime")
        private String endTime;

        @JsonProperty("orderNumber")
        private int orderNumber;

        @JsonProperty("comments")
        private String comments;

        @JsonProperty("minSupport")
        private double minSupport;

        // 省略getter和setter方法
        public int getTrainId() {
            return trainId;
        }

        public void setTrainId(int trainId) {
            this.trainId = trainId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(int orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public double getMinSupport() {
            return minSupport;
        }

        public void setMinSupport(double minSupport) {
            this.minSupport = minSupport;
        }
    }

    public static TrainHistoryQueryResponseData getTrainHistoryQueryResponseDataExample() {
        // 创建训练记录列表
        List<TrainHistoryQueryResponseData.TrainRecord> records = Arrays.asList(
                new TrainHistoryQueryResponseData.TrainRecord() {{
                    setTrainId(1);
                    setStartTime("2024-08-17 15:10:50");
                    setEndTime("2024-08-17 15:11:14");
                    setOrderNumber(1000);
                    setComments("null");
                    setMinSupport(0.09000000357627869);
                }},
                new TrainHistoryQueryResponseData.TrainRecord() {{
                    setTrainId(2);
                    setStartTime("2024-08-18 15:10:50");
                    setEndTime("2024-08-18 15:11:14");
                    setOrderNumber(1000);
                    setComments("null");
                    setMinSupport(0.08000000357627869);
                }}
        );

        // 创建响应体实例并设置数据
        TrainHistoryQueryResponseData response = new TrainHistoryQueryResponseData();
        response.setData(records);
        return response;
    }
}