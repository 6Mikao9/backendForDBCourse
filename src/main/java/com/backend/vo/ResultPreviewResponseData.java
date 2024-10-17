package com.backend.vo;

import java.util.*;

public class ResultPreviewResponseData {

    private Map<String, List<FrequentItemSet>> frequentItemSets;
    private Map<String, List<AssociationRule>> associationRules;

    public Map<String, List<FrequentItemSet>> getFrequentItemSets() {
        return frequentItemSets;
    }

    public Map<String, List<AssociationRule>> getAssociationRules() {
        return associationRules;
    }

    private void setAssociationRules(Map<String, List<AssociationRule>> associationRules) {
        this.associationRules = associationRules;
    }

    private void setFrequentItemSets(Map<String, List<FrequentItemSet>> frequentItemSets) {
        this.frequentItemSets = frequentItemSets;
    }

    // 省略getter和setter方法，为了简洁

    // 频繁项集类
    public static class FrequentItemSet {
        private Map<String, Object> ticketAttributes; // 机票属性，可能为null
        private Map<String, Object> itemAttributes; // 商品属性，可能为null
        private int freq; // 频次

        // getter和setter方法

        public Map<String, Object> getTicketAttributes() {
            return ticketAttributes;
        }

        public void setTicketAttributes(Map<String, Object> ticketAttributes) {
            this.ticketAttributes = ticketAttributes;
        }

        public Map<String, Object> getItemAttributes() {
            return itemAttributes;
        }

        public void setItemAttributes(Map<String, Object> itemAttributes) {
            this.itemAttributes = itemAttributes;
        }

        public int getFreq() {
            return freq;
        }

        public void setFreq(int freq) {
            this.freq = freq;
        }


    }

    // 关联规则类
    public static class AssociationRule {
        private Map<String, Object> antecedent; // 前件
        private String consequence; // 后件
        private double confidence; // 置信度

        // getter和setter方法

        public Map<String, Object> getAntecedent() {
            return antecedent;
        }

        public void setAntecedent(Map<String, Object> antecedent) {
            this.antecedent = antecedent;
        }

        public String getConsequence() {
            return consequence;
        }

        public void setConsequence(String consequence) {
            this.consequence = consequence;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }
    }

    public static ResultPreviewResponseData getPreviewResultExample(){
        ResultPreviewResponseData resultPreviewResponseData = new ResultPreviewResponseData();

        // 初始化FrequentItemSets
        Map<String, List<ResultPreviewResponseData.FrequentItemSet>> frequentItemSets = new HashMap<>();
        List<ResultPreviewResponseData.FrequentItemSet> baggageSets = new ArrayList<>();

        Map<String, Object> ticketAttrs1 = new HashMap<>();
        ticketAttrs1.put("attribute_name1", "attribute_value1");
        ticketAttrs1.put("attribute_name2", null);

        Map<String, Object> itemAttrs1 = new HashMap<>();
        itemAttrs1.put("attribute_name1", null);
        itemAttrs1.put("attribute_name2", "attribute_value2");

        ResultPreviewResponseData.FrequentItemSet set1 = new ResultPreviewResponseData.FrequentItemSet();
        set1.setTicketAttributes(ticketAttrs1);
        set1.setItemAttributes(itemAttrs1);
        set1.setFreq(21);

        ticketAttrs1.clear();
        ticketAttrs1.put("attribute_name1", "attribute_value1");
        ticketAttrs1.put("attribute_name2", null);

        itemAttrs1.clear();
        itemAttrs1.put("attribute_name1", "attribute_value1");
        itemAttrs1.put("attribute_name2", null);

        ResultPreviewResponseData.FrequentItemSet set2 = new ResultPreviewResponseData.FrequentItemSet();
        set2.setTicketAttributes(ticketAttrs1);
        set2.setItemAttributes(itemAttrs1);
        set2.setFreq(38);

        baggageSets.add(set1);
        baggageSets.add(set2);

        frequentItemSets.put("Baggage", baggageSets);
        //包含五个品类商品（Baggage，Hotel，Insurance，Meal，Seat），除baggage外对应空列表
        frequentItemSets.put("Hotel", new ArrayList<>());
        frequentItemSets.put("Insurance", new ArrayList<>());
        frequentItemSets.put("Meal", new ArrayList<>());
        frequentItemSets.put("Seat", new ArrayList<>());

        // 初始化AssociationRules
        Map<String, List<ResultPreviewResponseData.AssociationRule>> associationRules = new HashMap<>();
        List<ResultPreviewResponseData.AssociationRule> baggageRules = new ArrayList<>();

        Map<String, Object> antecedent1 = new HashMap<>();
        antecedent1.put("attribute_name1", "attribute_value1");
        antecedent1.put("attribute_name2", "attribute_value2");

        ResultPreviewResponseData.AssociationRule rule1 = new ResultPreviewResponseData.AssociationRule();
        rule1.setAntecedent(antecedent1);
        rule1.setConsequence("attribute_name:attribute_value");
        rule1.setConfidence(0.8571428656578064);

        antecedent1.clear();
        antecedent1.put("attribute_name1", "attribute_value1");
        antecedent1.put("attribute_name2", "attribute_value2");

        ResultPreviewResponseData.AssociationRule rule2 = new ResultPreviewResponseData.AssociationRule();
        rule2.setAntecedent(antecedent1);
        rule2.setConsequence("attribute_name:attribute_value");
        rule2.setConfidence(0.800000011920929);

        baggageRules.add(rule1);
        baggageRules.add(rule2);

        associationRules.put("Baggage", baggageRules);
        //包含五个品类商品（Baggage，Hotel，Insurance，Meal，Seat），除baggage外对应空列表
        associationRules.put("Hotel", new ArrayList<>());
        associationRules.put("Insurance", new ArrayList<>());
        associationRules.put("Meal", new ArrayList<>());
        associationRules.put("Seat", new ArrayList<>());

        // 设置到responseData
        resultPreviewResponseData.setFrequentItemSets(frequentItemSets);
        resultPreviewResponseData.setAssociationRules(associationRules);
        return resultPreviewResponseData;
    }


}