package ru.vsu.cs.course1.graph;

import java.util.Scanner;

public class Djk {
    //    Вариант 11. Достижимость за N.
//    По системе двусторонних дорог определить, есть ли в ней город, из которого можно
//    добраться в любой другой менее чем за N км. Разрешается построить не более K
//    дополнительных дорог (расстояния допустимых для строительства дорог задаются
//            отдельно). Не стоит привязываться к N как к расстоянию. Вместо него могут быть указаны
//    время в пути или же стоимость поездки. В связи с этим веса добавляемых рѐбер не зависят
//    от взаимного расположения узлов, но они не могут быть меньше нуля.
    public static void main(String[] args) {

        int n = 20;
        int k = 2; //кол во доп дорог
        int dopLength = 9; // длина доп дорог

        int[][] MatrixVeight = {{99, 20, 99, 15, 15},
                {20, 99, 15, 99, 99},
                {99, 15, 99, 12, 99},
                {15, 99, 12, 99, 20},
                {15, 99, 99, 20, 99}};
        int[][] dopMatrixVeight = copyArr(MatrixVeight);
        int[][] MatrixHistory = {{0, 2, 0, 4, 5},
                {1, 0, 3, 0, 0},
                {0, 2, 0, 4, 0},
                {1, 0, 3, 0, 5},
                {1, 0, 0, 4, 0}};
        int[][] dopMatrixHistory = copyArr(MatrixHistory);

        output(MatrixVeight);
        System.out.println("dops");
        output(dopMatrixHistory);

        int[][] distancies = floyd(MatrixVeight, MatrixHistory);
        output(distancies);

        System.out.println("Без доп дорог: " + hasCity(distancies, n));

        //output(MatrixVeight);
//        System.out.println("dops");
//        output(dopMatrixHistory);
        if (!hasCity(distancies, n)) {
            System.out.println("С доп дорогами: " + buildDop(dopMatrixVeight, dopMatrixHistory, k, dopLength, n));
        }


    }

    //строим доп дороги
    public static boolean buildDop(int[][] MatrixVeight, int[][] MatrixHistory, int k, int dopLength, int n) {
        int planRoads = 0;
        boolean findCity = false;
        while (planRoads <= k) {
            int[][] newMatrixRoads = copyArr(MatrixVeight);
            int[][] newMatrixHistory = copyArr(MatrixHistory);
            findCity = checkCityWithDopRoads(newMatrixRoads, newMatrixHistory, planRoads, k, n, dopLength);
            if (findCity) {
                return true;
            }
            planRoads++;
        }
        return false;
    }

    public static boolean checkCityWithDopRoads(int[][] newMatrixRoads, int[][] newMatrixHistory, int currNumOfDopRoads, int k, int n, int dopLength) {
        boolean result = false;
        if (currNumOfDopRoads == k) {
            int[][] roads = copyArr(newMatrixRoads);
            int[][] history = copyArr(newMatrixHistory);
            return hasCity(floyd(roads, history), n);
        } else {
            for (int i = 0; i < newMatrixRoads.length; i++) {
                for (int j = 0; j < newMatrixRoads.length; j++) {
                    if (i != j && newMatrixHistory[i][j] == 0) {
                        int old = newMatrixRoads[i][j];
                        newMatrixRoads[i][j] = dopLength;
                        newMatrixHistory[i][j] = j + 1;
                        result = checkCityWithDopRoads(newMatrixRoads, newMatrixHistory, currNumOfDopRoads + 1, k, n, dopLength);
                        if (result) {
                            return true;
                        } else { //если в строке сделали k дорог, то прошлые значения вернули в первоначальное состояние
                            newMatrixRoads[i][j] = old;
                            newMatrixHistory[i][j] = 0;
                        }
                    }
                }

            }
        }
        return false;
    }

    //алгоритм Флойда Уоршелла
    public static int[][] floyd(int[][] MatrixVeight, int[][] MatrixHistory) {
        for (int i = 0; i < MatrixVeight.length; i++) {
            for (int j = 0; j < MatrixVeight.length; j++) {
                if (MatrixVeight[i][j] > -1) {
                    for (int w = 0; w < MatrixVeight.length; w++) {
                        if (MatrixVeight[i][w] > MatrixVeight[i][j] + MatrixVeight[j][w]) {
                            MatrixVeight[i][w] = MatrixVeight[i][j] + MatrixVeight[j][w];
                            MatrixHistory[i][w] = MatrixHistory[i][j];
                        }
                    }

                }
            }
        }
        for (int i = 0; i < MatrixVeight.length; i++) {
            for (int j = 0; j < MatrixVeight.length; j++) {
                if (i == j) {
                    MatrixVeight[i][j] = 99;
                }
            }
        }
        return MatrixVeight;
    }


    //вывод матрицы
    public static void output(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                System.out.printf("%3d", a[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //проверка условия достижимости
    public static boolean hasCity(int[][] distancies, int n) {
        for (int i = 0; i < distancies.length; i++) {
            int calk = 1;
            for (int j = 0; j < distancies.length; j++) {
                if (i == j) {
                    continue;
                } else if (distancies[i][j] < n) {
                    calk++;
                } else break;
            }
            if (calk == distancies.length) {
                return true;
            }
        }
        return false;
    }

    public static int[][] copyArr(int[][] a) {
        int[][] b = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                b[i][j] = a[i][j];
            }
        }
        return b;
    }
}