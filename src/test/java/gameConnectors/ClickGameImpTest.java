package gameConnectors;

import database.DAManager;
import database.DAOGameResults;
import database.User;
import database.UserDAO;
import game.Board;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import resources.GameInfoResource;
import resources.ResourceProvider;
import resources.ResponseResource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DAManager.class)
public class ClickGameImpTest {
    private MockEndpoint r1;
    private MyWebSocket ws1, ws2;

    private static DAManager mgr;
    private static DAOGameResults daoGameResults;
    private static UserDAO dao;
    private User usr1;
    private ResponseResource responseResource;
    private GameInfoResource setup;

    private static final String usr1Name = "vasya", usr2Name = "fedya",
    usr1Pass = "pwd1", usr2Pass = "pwd2",
    usr1Mail = "a@m", usr2Mail = "b@m";

    @BeforeClass
    public static void initClass() {
        SessionFactory dbSF = database.UserDAOTest.getFactory(false);
        dao = new UserDAO(dbSF);
        daoGameResults = new DAOGameResults(dbSF);
        dao.saveUser(new User(usr1Name, usr1Pass, usr1Mail));
        dao.saveUser(new User(usr2Name, usr2Pass, usr2Mail));
        mgr = mock(DAManager.class);
    }

    @Before
    public void initialize() {
        r1 = getMockEndpoint();
        MockEndpoint r2 = getMockEndpoint();
        Session s1 = getMockSession(r1);
        Session s2 = getMockSession(r2);
        usr1 = dao.findUser(usr1Name);
        User usr2 = dao.findUser(usr2Name);
        ws1 = getMockWebSocket(s1, usr1);
        ws2 = getMockWebSocket(s2, usr2);
        responseResource = (ResponseResource) ResourceProvider.getProvider().getResource("response_resource.xml");
        setup = (GameInfoResource) ResourceProvider.getProvider().getResource("game_info.xml");

        PowerMockito.mockStatic(DAManager.class);
        Mockito.when(DAManager.getSingleton()).thenReturn(mgr);

        when(mgr.getUserDAO()).thenReturn(dao);
        when(mgr.getGameResultsDAO()).thenReturn(daoGameResults);
    }

    private MockEndpoint getMockEndpoint() {
        return new MockEndpoint();
    }

    private Session getMockSession(RemoteEndpoint mockEndpoint) {
        Session s = mock(Session.class);
        when(s.getRemote()).thenReturn(mockEndpoint);
        when(s.isOpen()).thenReturn(true);
        return s;
    }

    private MyWebSocket getMockWebSocket(Session mockSession, User user) {
        MyWebSocket ws = new WebSocketImp(user);
        ws.setSession(mockSession);
        return ws;
    }

    private static void capture(ClickGameImp gi, int row, int col, MyWebSocket from) {
        JSONObject request = new JSONObject();
        request.put("row", row);
        request.put("col", col);
        gi.dispatchMessage(from, request.toString());
    }

    @Test
    public void testConnect() {
        new ClickGameImp(ws1, ws2);
        JSONObject resp = new JSONObject(r1.getLastSent());
        assertEquals("Expected to get GameStart status",
                setup.getGameStartStatus(), resp.getString(responseResource.getStatus()));
    }

    @Test
    public void testCapture() {
        ClickGameImp gi = new ClickGameImp(ws1, ws2);
        capture(gi, 0, 0, ws1);
        JSONObject resp = new JSONObject(r1.getLastSent());
        assertEquals("Expected to get captured message",
                setup.getCommandAcceptedMessage(), resp.getString(responseResource.getMessage()));
    }

    @Test
    public void testDisconnect() {
        new ClickGameImp(ws1, ws2);
        ws2.close();
        JSONObject resp = new JSONObject(r1.getLastSent());
        assertEquals("Expected to get disconnected status",
                setup.getConnectCloseStatus(), resp.getString(responseResource.getStatus()));
    }

    @Test
    public void testGameEnd() {
        ClickGameImp gi = new ClickGameImp(ws1, ws2);
        Board b = gi.getBoard();
        Integer[][] captureArray = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        };
        game.BoardTest.capture(captureArray, b);
        capture(gi, 9, 9, ws2);
        JSONObject resp = new JSONObject(r1.getLastSent());
        assertEquals("Expected to get game end status",
                setup.getGameEndStatus(), resp.getString(responseResource.getStatus()));
    }

    @Test
    public void testResultsSave() {
        ClickGameImp gi = new ClickGameImp(ws1, ws2);
        Board b = gi.getBoard();
        Integer[][] captureArray = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 2, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
        };
        game.BoardTest.capture(captureArray, b);
        usr1 = dao.findUser(usr1Name);
        int resultsBefore = usr1.getGameResults().size();
        capture(gi, 9, 9, ws2);
        usr1 = dao.findUser(usr1Name);
        int resultsAfter = usr1.getGameResults().size();
        assertEquals("Expected to get more results than before save game",
                resultsBefore+1, resultsAfter);
    }
}