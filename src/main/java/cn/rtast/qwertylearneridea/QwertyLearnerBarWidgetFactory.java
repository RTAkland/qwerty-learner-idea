package cn.rtast.qwertylearneridea;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.NotNull;

public class QwertyLearnerBarWidgetFactory implements StatusBarWidgetFactory {
    @Override
    public @NotNull String getId() {
        return "QwertyLearnerBarWidgetFactory";
    }

    @Override
    public @NotNull String getDisplayName() {
        return "QwertyLearner";
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        return new QwertyLearnerBarWidget(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
        widget.dispose();
    }
}
