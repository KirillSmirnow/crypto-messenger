package cryptomessenger.desktop.infrastructure.ui.controller;

import cryptomessenger.desktop.infrastructure.ui.Refreshable;
import cryptomessenger.desktop.infrastructure.ui.dialog.SendMessageDialog;
import cryptomessenger.desktop.service.message.Message;
import cryptomessenger.desktop.service.message.MessageService;
import cryptomessenger.desktop.service.user.UserService;
import cryptomessenger.desktop.utility.ThreadFactories;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MainSceneController implements Refreshable {

    private final UserService userService;
    private final MessageService messageService;
    private final SendMessageDialog sendMessageDialog;

    public TextField usernameField;
    public TableView<Message> inboxTable;
    public TableView<Message> outboxTable;

    private ScheduledExecutorService executor;

    @Override
    public void refresh() {
        usernameField.setText(userService.getCurrentUsername());
        refreshInboxTable();
        refreshOutboxTable();
        configureAutoRefresh();
    }

    private void refreshInboxTable() {
        var columns = inboxTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("senderUsername"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("sentAt"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("text"));
        inboxTable.setItems(FXCollections.observableArrayList(
                messageService.getInbox(Pageable.ofSize(100).withPage(0)).getContent()
        ));
    }

    private void refreshOutboxTable() {
        var columns = outboxTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("receiverUsername"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("sentAt"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("text"));
        outboxTable.setItems(FXCollections.observableArrayList(
                messageService.getOutbox(Pageable.ofSize(100).withPage(0)).getContent()
        ));
    }

    private void configureAutoRefresh() {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(1, ThreadFactories.daemon());
            executor.scheduleWithFixedDelay(this::refreshInboxTable, 1, 1, TimeUnit.SECONDS);
        }
    }

    public void onRegister(ActionEvent actionEvent) {
        userService.register(usernameField.getText());
        refresh();
    }

    public void onSendMessage(ActionEvent actionEvent) {
        sendMessageDialog.show(this::refresh);
    }

    public void onRefresh(ActionEvent actionEvent) {
        refresh();
    }
}
