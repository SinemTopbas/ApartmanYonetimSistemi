public class IdleState implements DashboardState {

    @Override
    public void sendNotification(UserDashboard context) {
        context.setState(new NotificationSendingState());
        context.getState().sendNotification(context);
    }

    @Override
    public void showNotifications(UserDashboard context) {
        context.setState(new NotificationViewingState());
        context.getState().showNotifications(context);
    }
}
