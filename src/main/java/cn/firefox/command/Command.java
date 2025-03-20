package cn.firefox.command;

import cn.firefox.Wrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LangYa466
 * @since 2025/3/20
 */
@Getter
@Setter
@AllArgsConstructor
public class Command implements Wrapper {
    private final String name, usage;

    public boolean run(String[] args) {
        return false;
    }
}
