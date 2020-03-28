package view;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Visual {
    private static JPanel panel = new JPanel(new GridLayout(0, 2,1,1));//Составление заказа
    private static JPanel panelOrder = new JPanel(new GridLayout(0, 2,1,1));//Отображение заказа
    private static JLabel lblType = new JLabel("Вид:");
    private static JLabel lblSize = new JLabel("Размер:");
    private static JLabel lblColor = new JLabel("Цвет:");
    private static JLabel lblCount = new JLabel("Количество:");
    private static JLabel lblPrice = new JLabel("Cost for one:");
    private static JLabel lblInfo = new JLabel("Справка:");
    private static JLabel lblPriceForAll = new JLabel("Total cost:");
    private static JButton bNewOrder = new JButton("Новый заказ");
    private static JLabel lblFinalTotalCost = new JLabel("Общая сумма");

    static void createAndShowGui() {
        Listeners.jFrame.setSize(600, 500);
        Listeners.jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Listeners.jFrame.setLayout(new GridLayout(0, 2, 0, 0));

        panel.setBorder(BorderFactory.createTitledBorder("Составление заказа"));
        panelOrder.setBorder(BorderFactory.createTitledBorder("Ваш заказа"));

        SpringLayout springLayout = new SpringLayout();
        panel.setLayout(springLayout);

        panel.add(lblType);
        panel.add(Listeners.cmbType);
        panel.add(lblColor);
        panel.add(Listeners.cmbColor);
        panel.add(lblSize);
        panel.add(Listeners.cmbSize);
        panel.add(lblPrice);
        panel.add(Listeners.fldPrice);
        panel.add(lblCount);
        panel.add(Listeners.fldCount);
        PlainDocument document = (PlainDocument) Listeners.fldCount.getDocument();
        document.setDocumentFilter(new DigitFilter()); // устаовка фильтра для ввода только числовых значений
        panel.add(lblPriceForAll);
        panel.add(Listeners.fldPriceForAll);
        panel.add(lblInfo);

        panel.add(new JScrollPane(Listeners.areaInfo));
        Listeners.areaInfo.setEditable(false);
        Listeners.areaInfo.setLineWrap(true);

        panel.add(bNewOrder);
        panel.add(Listeners.bAddToOrder);

        Helper.makeCompactGrid(panel,8,2,6,6,6,6);

        panelOrder.setLayout(springLayout);
        panelOrder.add(new JScrollPane(Listeners.orderJList));
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.add(lblFinalTotalCost,BorderLayout.WEST);
        panel1.add(Listeners.fldFinalTotalCost,BorderLayout.EAST);
        panelOrder.add(panel1);
        panelOrder.add(Listeners.bRemoveFromOrder);
        panelOrder.add(Listeners.bReadyOrder);


        Helper.makeCompactGrid(panelOrder,4,1, 6,6,6,6);
        // выключение кнопок
        Listeners.bRemoveFromOrder.setEnabled(false);
        Listeners.bReadyOrder.setEnabled(false);
        Listeners.bAddToOrder.setEnabled(false);
        Listeners.cmbType.setEnabled(false);
        Listeners.cmbColor.setEnabled(false);
        Listeners.cmbSize.setEnabled(false);
        Listeners.fldCount.setEnabled(false);
        Listeners.fldPriceForAll.setEnabled(false);

        Listeners.jFrame.add(panel);
        Listeners.jFrame.add(panelOrder);
        Listeners.jFrame.pack();
        Listeners.jFrame.setVisible(true);
        JOptionPane.showMessageDialog(Listeners.jFrame,"Для начала работы нажмите на кнопку \"Новый заказ\"","Информация",JOptionPane.INFORMATION_MESSAGE);
        for (Object f : Helper.nameOfFlowers) { // добавление в JComboBox
            Listeners.cmbType.addItem(f);
        }
        Listeners.cmbType.setSelectedItem(null);


        // листнер для комбобокса с выбором вида цветов
        Listeners.cmbType.addActionListener(Listeners::getTypeCmb);
        // установка размера в завиcимоcти от типа
        Listeners.cmbColor.addActionListener(Listeners::getColorCmb);
        // устаовка стоимости в зависимости от выбора размера
        Listeners.cmbSize.addActionListener(Listeners::getSizeCmb);

        // подсчет общей суммы от введенного значения количества
        Listeners.fldCount.addCaretListener(Listeners::getCaretNumb);
        ArrayList<Order> orderList = new ArrayList<>();//лист с добавление заказов
        DefaultListModel model = new DefaultListModel();// дефолтный лист в который можно засовывать заказы
        Listeners.orderJList.setModel(model);

        //обработка нового заказа
        bNewOrder.addActionListener(e -> Listeners.addNewOrder(model, orderList));
        //добавление заказа в очередь
        Listeners.bAddToOrder.addActionListener(e -> Listeners.addToOrder(orderList, model));

        Listeners.bRemoveFromOrder.addActionListener(new ActionListener() {// удаление из списка заказа
            @Override
            public void actionPerformed(ActionEvent e) {
                Listeners.removeFromOrder(orderList, model);
            }
        });

        // TODO: 07.11.2019  оформление заказасделать в виде диалогово окна.
        Listeners.bReadyOrder.addActionListener(e -> Listeners.readyOrder(orderList));
    }
}
