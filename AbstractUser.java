public abstract class AbstractUser {
    private String name;

    public AbstractUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void displayRole();
}

class Manager extends AbstractUser {
    public Manager(String name) {
        super(name);
    }

    @Override
    public void displayRole() {
        System.out.println(getName() + " Bir Yönetici.");
    }
}

class UserFactory {
    public static AbstractUser createUser(String userType, String name) {
        if (userType.equalsIgnoreCase("Yönetici")) {
            return new Manager(name);
        }
        return null;
    }
}
