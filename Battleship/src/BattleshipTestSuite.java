import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ArenaTest.class, PlayerTest.class, SavingTest.class, ShipSetupControllerTest.class, ShipsTest.class })
public class BattleshipTestSuite {

}
