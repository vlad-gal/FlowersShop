package view;

import flowers.Dandelion;
import flowers.Flower;
import flowers.Rose;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class Listeners {
    private static Order order;
    //визуальное оформление
    static JFrame jFrame = new JFrame("FlowErs");
    static JComboBox cmbType = new JComboBox<>();
    static JComboBox cmbSize = new JComboBox<>();
    static JComboBox cmbColor = new JComboBox<>();
    static JTextField fldCount = new JTextField();
    static JLabel fldPrice = new JLabel();
    static JTextArea areaInfo = new JTextArea();
    static JLabel fldPriceForAll = new JLabel();
    static JButton bAddToOrder = new JButton("Добавить к заказу");
    static JButton bRemoveFromOrder = new JButton("Удалить из заказа");
    static JButton bReadyOrder = new JButton("Оформить заказа");
    static JList orderJList = new JList();
    static JLabel fldFinalTotalCost = new JLabel();

    public static void readyOrder(ArrayList<Order> orderList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Order o: Helper.getOrderList(orderList)) {
            //System.out.println(o.toString());
            stringBuilder.append(o+"\n");

        }
        int n = JOptionPane.showConfirmDialog(jFrame,stringBuilder,"Order",JOptionPane.OK_CANCEL_OPTION);

        if (n == JOptionPane.YES_OPTION){
            JOptionPane.showMessageDialog(jFrame,"Заказ выполнен","Информация",JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public static void removeFromOrder(ArrayList<Order> orderList, DefaultListModel model) {
        orderList.remove(orderJList.getSelectedIndex());
        model.remove(orderJList.getSelectedIndex());

        double finalTotalCost = 0;

        for (Order r: orderList) {
            finalTotalCost += r.getFlower().getCost()*r.getCount();
        }
        fldFinalTotalCost.setText(String.valueOf(finalTotalCost));

        if (model.isEmpty()){
            bReadyOrder.setEnabled(false);
            bRemoveFromOrder.setEnabled(false);
            bAddToOrder.setEnabled(false);
        }
    }

    public static void addToOrder(ArrayList<Order> orderList, DefaultListModel model) {
        String type = cmbType.getSelectedItem().toString();
        String color = cmbColor.getSelectedItem().toString();
        String size = cmbSize.getSelectedItem().toString();
        double cost = Double.parseDouble(fldPrice.getText());
        String description = areaInfo.getText();
        int count = Integer.parseInt(fldCount.getText());
        Flower fl = null;
        if (type.equals("Роза")){ // 07.11.2019 дописать для всех видов + проверка на нулевое количество цветов
            fl = new Rose(type,color,size,cost,description);
        } else if (type.equals("Одуванчик")){
            fl = new Dandelion(type,color,size,cost,description);
        }
        if (count == 0){
            return;
        }
        order = new Order(fl,count);

        orderList.add(order);

        //System.out.println(order.toString());
        model.addElement(order);
        ArrayList<Order> temp = Helper.getOrderList(orderList);
        if (model.contains(order)){ // это объединяет одинаковые заказы
            model.removeAllElements();
            orderList.clear();
            for (Order r:temp) {
                orderList.add(r);
                model.addElement(r);
            }
        }

        cmbType.setSelectedItem(null);
        cmbSize.setSelectedItem(null);
        cmbColor.setSelectedItem(null);
        fldPrice.setText("");
        fldCount.setText("");
        fldPriceForAll.setText("");
        fldFinalTotalCost.setText("");
        areaInfo.setText("");


        double finalTotalCost = 0;

        for (Order r: orderList) {
            finalTotalCost += r.getFlower().getCost()*r.getCount();
        }
        fldFinalTotalCost.setText(String.valueOf(finalTotalCost));

        bReadyOrder.setEnabled(true);
        bRemoveFromOrder.setEnabled(true);
        cmbSize.setEnabled(false);
        cmbColor.setEnabled(false);
        fldCount.setEnabled(false);
    }

    public static void addNewOrder(DefaultListModel model, ArrayList<Order> orderList) {
        JOptionPane.showMessageDialog(jFrame,"Новый заказ! Старый будет удален!","Новый заказ",JOptionPane.INFORMATION_MESSAGE);
        //System.out.println("Привет");
        cmbType.setSelectedItem(null);
        cmbSize.setSelectedItem(null);
        cmbColor.setSelectedItem(null);
        fldPrice.setText("");
        fldCount.setText("");
        fldPriceForAll.setText("");
        fldFinalTotalCost.setText("");
        areaInfo.setText("");
        model.removeAllElements();
        orderList.clear();
        //выключение кнопок
        bRemoveFromOrder.setEnabled(false);
        bReadyOrder.setEnabled(false);
        bAddToOrder.setEnabled(false);

        cmbType.setEnabled(true);

    }

    public static void getCaretNumb(CaretEvent e) {
        if (e.getDot()==0){
            fldCount.setText("");
            fldPriceForAll.setText("");
        } else{
            JTextField field = (JTextField) e.getSource();
            String temp = field.getText();
            double tempTotalprice = Double.parseDouble(temp);
            double tempPrice = Double.parseDouble(fldPrice.getText());
            fldPriceForAll.setText(String.format(Locale.ENGLISH,"%.2f",tempTotalprice*tempPrice));
            bAddToOrder.setEnabled(true);

        }
    }

    public static void getSizeCmb(ActionEvent e) {
        JComboBox temp = (JComboBox) e.getSource();
        String tempSize = (String) temp.getSelectedItem();

        for (Flower fl : Helper.flowers) {
            if (fl.getSize().equals(tempSize)) {
                fldPrice.setText(String.valueOf(fl.getCost()));
                areaInfo.setText(fl.getDescription());
            }
        }
        fldCount.setEnabled(true);
        fldPriceForAll.setEnabled(true);
    }

    public static void getColorCmb(ActionEvent e) {
        JComboBox temp = (JComboBox) e.getSource();
        String tempColor = (String) temp.getSelectedItem();
        ArrayList flowerTempSize = new ArrayList<>();

        for (Flower fl : Helper.flowers) {
            if (fl.getColor().equals(tempColor)) {
                flowerTempSize.add(fl.getSize());
                flowerTempSize = (ArrayList) flowerTempSize.stream().distinct().collect(Collectors.toList()); // удаление дубликтов с размером цветка
                DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(flowerTempSize.toArray());// назначаем комбобоксу с размером
                cmbSize.setModel(defaultComboBoxModel);
                cmbSize.setSelectedItem(null);
                cmbSize.setEnabled(true);
            }
        }
    }

    public static void getTypeCmb(ActionEvent e) {
        JComboBox temp = (JComboBox) e.getSource();
        String strTemp = (String) temp.getSelectedItem();
        ArrayList flowerTempColor = new ArrayList<>();

        for (Flower fl : Helper.flowers) {
            if (fl.getType().equals(strTemp)) {
                flowerTempColor.add(fl.getColor());
                flowerTempColor = (ArrayList) flowerTempColor.stream().distinct().collect(Collectors.toList()); // удаление дубликтов с типом цвета
                DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel(flowerTempColor.toArray());// назначаем комбобоксу с цветом
                cmbColor.setModel(defaultComboBoxModel);
                cmbColor.setSelectedItem(null);
                cmbSize.setSelectedItem(null);
                cmbColor.setEnabled(true);
            }
        }
    }
}
