package org.solideinc.solidequerydistributor.Util;

import org.solideinc.solidequerydistributor.Abstract.AbstractPageLoader;

public class PageLoader extends AbstractPageLoader {
    @Override
    public void loadFirstPage() {
        loadLoginPage();
    }

    @Override
    public void loadLoginPage() {
        loadPage("Login.fxml", "Solide™ Query Distributor - Login");
    }

    @Override
    public void loadMainPage() {
        loadPage("Main.fxml", "Solide™ Query Distributor - Main");
    }

    @Override
    public void loadAccountPage() {
        loadPage("Account.fxml", "Solide™ Query Distributor - Account");
    }
}