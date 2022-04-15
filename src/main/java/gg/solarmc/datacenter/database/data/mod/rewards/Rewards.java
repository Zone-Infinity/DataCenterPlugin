package gg.solarmc.datacenter.database.data.mod.rewards;

import gg.solarmc.datacenter.database.DataCenter;
import gg.solarmc.datacenter.database.data.single.SingleData;

import java.time.Instant;
import java.util.List;

public class Rewards extends SingleData<String> {

    public Rewards(DataCenter center, String uuid, String value) {
        super(center, uuid, value);
    }

    @Override
    public String get() {
        if (value != null) return value;

        value = getValue(RewardsKey.INSTANCE.getConstants(), "-", String.class);
        RewardsKey.INSTANCE.getCache().put(uuid, value);
        return value;
    }

    @Override
    public void set(String value) {
        this.value = value;
        RewardsKey.INSTANCE.getCache().put(uuid, value);
        setValue(RewardsKey.INSTANCE.getConstants(), value);
    }

    public CoolDown getCoolDown(String key) {
        return CoolDown.deserialize(get())
                .stream()
                .filter(it -> it.getKey().equals(key))
                .findAny()
                .orElse(new CoolDown(key, 0L));
    }

    public void setCoolDown(String key, long expireCoolDown) {
        List<CoolDown> coolDowns = CoolDown.deserialize(get());
        coolDowns.removeIf(it -> it.getKey().equals(key));

        long expireTime = Instant.now().toEpochMilli() + expireCoolDown;
        coolDowns.add(new CoolDown(key, expireTime));

        set(CoolDown.serialize(coolDowns));
    }
}
