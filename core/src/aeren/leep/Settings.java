package aeren.leep;

public class Settings {
    private static Settings instance;

    private int volume = 1;

    private Settings() {

    }

    public static Settings getInstance() {
        if (instance == null)
            instance = new Settings();

        return instance;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
        DataManager.getInstance().setData("volume", volume);
    }

    public void update() {
        volume = DataManager.getInstance().getInt("volume");
    }
}
