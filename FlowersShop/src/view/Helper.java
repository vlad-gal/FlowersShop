package view;

import flowers.Dandelion;
import flowers.Flower;
import flowers.Rose;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Helper {
    // листы для хранения переменных в процессе работы кода
    public static ArrayList<Flower> flowers = new ArrayList<>();
    public static ArrayList nameOfFlowers = new ArrayList();

    public static void loadData() {
        ArrayList arrayList = new ArrayList(); //чтение из файла базы данных
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\vlad\\IdeaProjects\\ShopFlower\\FlowersShop\\src\\data.txt"));
            String line = reader.readLine();
            while (line != null) {
                arrayList.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringTokenizer st; // обработка данных прочтенных из файла бд
        for (int i = 0; i < arrayList.size(); i++) {
            st = new StringTokenizer((String) arrayList.get(i), ";");
            String name = st.nextToken();
            if (name.equals("Роза")) {
                Rose rose = new Rose(name, st.nextToken(), st.nextToken(), Double.parseDouble(st.nextToken()), st.nextToken());
                flowers.add(rose);
                nameOfFlowers.add(rose.getType());
            } else if (name.equals("Одуванчик")) {
                Dandelion dandelion = new Dandelion(name, st.nextToken(), st.nextToken(), Double.parseDouble(st.nextToken()), st.nextToken());
                flowers.add(dandelion);
                nameOfFlowers.add(dandelion.getType());
            }
        }
        nameOfFlowers = (ArrayList) nameOfFlowers.stream().distinct().collect(Collectors.toList()); // удаление дубликтов с типом цветка
    }

    public static ArrayList<Order> getOrderList(ArrayList<Order> orders) { // метод для объединения одинаковых заказов
        ArrayList<Order> temp = new ArrayList<>();
        ArrayList<Flower> flowers = new ArrayList<>();
        ArrayList counts = new ArrayList();
        for (Order r : orders) {
            flowers.add(r.getFlower());
            counts.add(r.getCount());
        }
        HashMap<Flower, Integer> map1 = new HashMap<>();
        Integer t1;
        for (Flower fl : flowers) {
            t1 = map1.get(fl);
            map1.put(fl, t1 == null ? 1 : t1 + 1);
        }

        for (Map.Entry entry: map1.entrySet()) {
            int r = 0;
            Flower d = null;
            for (int i = 0; i < orders.size(); i++) {
                if (entry.getKey().equals(orders.get(i).getFlower())){
                    r +=orders.get(i).getCount();
                    d = orders.get(i).getFlower();
                }
            }
            temp.add(new Order(d,r));
        }
        return temp;
    }

    private static SpringLayout.Constraints getConstraintsForCell(
            int row, int col,
            Container parent,
            int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).
                                getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).
                                getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
