package ooad.mazeobserver.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MazeObserverStepDefs {

    @Given("a fully-connected maze with {int} rooms")
    public void aFullyConnectedMazeWithRooms(Integer int1) {
        System.out.println("Maze with " + int1 + " rooms");
    }

    @When("the game sends an update message of {string}")
    public void theGameSendsAnUpdateMessageOf(String string) {
        System.out.println("Update message: " + string);
    }

    @Then("the maze displays the message")
    public void theMazeDisplaysWithThisMessage() {
        System.out.println("Maze displayed");
    }
}
