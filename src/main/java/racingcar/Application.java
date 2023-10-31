package racingcar;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        InputView input = new InputView();
        OutputView output = new OutputView();

        GameSystem system = new GameSystem();
        system.start(input, output);

    }
}


class GameSystem {
    private TotalCar totalCar;
    private int attempt;

    public void start(InputView input, OutputView output) {
        InputManufacture manufacture = new InputManufacture();
        output.askName();
        totalCar = makeCars(input, manufacture);
        output.askTry();
        attempt = makeTryNumber(input, manufacture);
        output.showLine();
        runGame(output);
        showWinner(output);
    }

    public TotalCar makeCars(InputView input, InputManufacture manufacture) {
        List<String> nameList = makeCarNames(input, manufacture);
        return saveCarList(nameList);
    }

    public ArrayList<String> makeCarNames(InputView input, InputManufacture manufacture) {
        String names = input.readCarNames();
        return manufacture.makeNameList(names);
    }

    public TotalCar saveCarList(List<String> nameList) {
        TotalCar totalCar = new TotalCar();

        for (String name : nameList) {
            totalCar.saveNewCar(name);
        }
        return totalCar;
    }

    public int makeTryNumber(InputView input, InputManufacture manufacture) {
        String attempt = input.readTryNumber();
        return manufacture.changeIntegerNumber(attempt);
    }

    public void runGame(OutputView output) {
        CarGame game = new CarGame(output);
        game.run(attempt, totalCar);
    }

    public void showWinner(OutputView output) {
        output.showWinnerNotice();
        output.showColon();
        Winner winner = new Winner();
        List<String> winnerList = winner.who(totalCar);
        output.showWinners(winnerList);
    }

}

class InputView {
    public String readCarNames() {
        String input = Console.readLine();
        runNameValidator(input);
        return input;
    }

    public String readTryNumber() {
        String number = Console.readLine();
        runTryValidator(number);
        return number;
    }

    private void runNameValidator(String input) {
        NameValidation.checkWord(input);
    }

    private void runTryValidator(String input) {
        TryValidation tryValidation = new TryValidation();
        tryValidation.checkTryNumber(input);
    }

}

class OutputView {
    private final static String NAME_MESSAGE = "경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)";
    private final static String TRY_MESSAGE = "시도할 회수는 몇회인가요?";
    private final static String RESULT_MESSAGE = "실행 결과";
    private final static String WINNER_MESSAGE = "최종 우승자";
    private final static String COLON = " : ";
    private final static String MOVE_DISTANCE = "-";
    private final static String COMMA = ",";

    public void askName() {
        System.out.println(NAME_MESSAGE);
    }

    public void askTry() {
        System.out.println(TRY_MESSAGE);
    }

    public void showResultNotice() {
        System.out.println(RESULT_MESSAGE);
    }

    public void showName(String name) {
        System.out.print(name);
    }

    public void showColon() {
        System.out.print(COLON);
    }

    public void showMove(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(MOVE_DISTANCE);
        }
    }

    public void showWinnerNotice() {
        System.out.print(WINNER_MESSAGE);
    }

    public void showLine() {
        System.out.println();
    }

    public void showWinners(List<String> winnerList) {
        for (int name = 0; name < winnerList.size(); name++) {
            System.out.print(winnerList.get(name));
            if (winnerList.size() > name + 1) {
                System.out.print(COMMA);
            }
        }
    }

}

class NameValidation {
    public static void checkWord(String input) {
        if (!input.matches("^([a-zA-Z]{1,5})(,[a-zA-Z]{1,5})*$")) {
            throw new IllegalArgumentException();
        }
    }
}

class TryValidation {
    public void checkTryNumber(String input) {
        if (!input.matches("^[1-9][0-9]*$")) {
            throw new IllegalArgumentException();
        }
    }

}

class InputManufacture {
    public ArrayList<String> makeNameList(String data) {
        String[] splitData = data.split(",");
        return new ArrayList<>(Arrays.asList(splitData));
    }

    public int changeIntegerNumber(String attempt) {
        return Integer.parseInt(attempt);
    }
}

class CarGame {
    private OutputView output;

    public CarGame(OutputView output) {
        this.output = output;
    }

    public void run(int attempt, TotalCar totalCar) {
        output.showResultNotice();
        for (int move = 0; move < attempt; move++) {
            saveResult(totalCar);
            showResult(totalCar);
            output.showLine();
        }
    }

    public void saveResult(TotalCar totalCar) {
        totalCar.moveDistance();
    }

    public void showResult(TotalCar totalCar) {
        List<String> names = totalCar.nameList();
        List<Integer> distances = totalCar.distanceList();
        for (int result = 0; result < names.size(); result++) {
            output.showName(names.get(result));
            output.showColon();
            output.showMove(distances.get(result));
            output.showLine();
        }
    }


}

class TotalCar {
    private List<Car> carList = new ArrayList<>();

    public void saveNewCar(String name) {
        Car car = new Car(name);
        carList.add(car);
    }

    public void moveDistance() {
        CarCondition condition = new CarCondition();
        for (Car c : carList) {
            if (condition.isMove()) {
                c.updateDistance(1);
            }
        }
    }

    public List<String> nameList() {
        List<String> carNames = new ArrayList<>();
        for (Car c : carList) {
            carNames.add(c.getName());
        }
        return carNames;
    }

    public List<Integer> distanceList() {
        List<Integer> carDistance = new ArrayList<>();
        for (Car c : carList) {
            carDistance.add(c.getDistance());
        }
        return carDistance;
    }

}


class Car {
    private String name;
    private int distance = 0;

    public Car(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void updateDistance(int addMoving) {
        this.distance = distance + addMoving;
    }

    public int getDistance() {
        return distance;
    }


}

class CarCondition {

    public boolean isMove() {
        if (makeRandom() >= 4) {
            return true;
        }
        return false;
    }

    public int makeRandom() {
        return Randoms.pickNumberInRange(0, 9);
    }


}

class Winner {
    private int maxDistance = 0;

    public List<String> who(TotalCar totalCar) {
        carCompare(totalCar.distanceList());
        List<String> winnerList = findWinner(totalCar.nameList(), totalCar.distanceList());
        return winnerList;
    }

    public void carCompare(List<Integer> carDistance) {
        for (int result = 0; result < carDistance.size(); result++) {
            if (distanceCompare(carDistance.get(result))) {
                maxDistance = carDistance.get(result);
            }
        }
    }

    public boolean distanceCompare(int distance) {
        if (maxDistance <= distance) {
            return true;
        }
        return false;
    }

    public List<String> findWinner(List<String> nameList, List<Integer> carDistance) {
        List<String> winnerList = new ArrayList<>();
        for (int order = 0; order < nameList.size(); order++) {
            if (maxDistance == carDistance.get(order)) {
                winnerList.add(nameList.get(order));
            }
        }
        return winnerList;
    }


}

