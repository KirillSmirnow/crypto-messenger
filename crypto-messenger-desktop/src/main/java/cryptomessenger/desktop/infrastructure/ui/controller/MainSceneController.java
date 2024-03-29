package cryptomessenger.desktop.infrastructure.ui.controller;

import cryptomessenger.desktop.infrastructure.client.websocket.WebSocketManager;
import cryptomessenger.desktop.infrastructure.client.websocket.handler.NewMessageHandler;
import cryptomessenger.desktop.infrastructure.ui.Refreshable;
import cryptomessenger.desktop.infrastructure.ui.dialog.SendMessageDialog;
import cryptomessenger.desktop.service.message.Message;
import cryptomessenger.desktop.service.message.MessageService;
import cryptomessenger.desktop.service.user.UserService;
import cryptomessenger.desktop.utility.Player;
import cryptomessenger.desktop.utility.ThreadFactories;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MainSceneController implements Refreshable {

    private final UserService userService;
    private final MessageService messageService;
    private final SendMessageDialog sendMessageDialog;
    private final NewMessageHandler newMessageHandler;
    private final WebSocketManager webSocketManager;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, ThreadFactories.daemon());

    public TextField usernameField;
    public Button registerButton;
    public TableView<Message> inboxTable;
    public TableView<Message> outboxTable;
    public Pagination inboxTablePagination;
    public Pagination outboxTablePagination;
    public Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
        configureAutoRefresh();
        newMessageHandler.setOnNewMessage(() -> Player.playAudio("/sounds/new-message.mp3"));
    }

    private void configureAutoRefresh() {
        executor.scheduleWithFixedDelay(() -> Platform.runLater(() -> {
            refreshInboxTable();
            refreshStatusLabel();
        }), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void refresh() {
        var username = userService.getCurrentUsername();
        usernameField.setText(username);
        usernameField.setDisable(!username.isEmpty());
        registerButton.setVisible(username.isEmpty());
        refreshInboxTable();
        refreshOutboxTable();
        refreshStatusLabel();
        webSocketManager.refreshSubscriptions();
    }

    private void refreshInboxTable() {
        TableConfigurer.builder()
                .table(inboxTable)
                .pagination(inboxTablePagination)
                .recipientField("senderUsername")
                .messagesProvider(messageService::getInbox)
                .build()
                .configure();
    }

    private void refreshOutboxTable() {
        TableConfigurer.builder()
                .table(outboxTable)
                .pagination(outboxTablePagination)
                .recipientField("receiverUsername")
                .messagesProvider(messageService::getOutbox)
                .build()
                .configure();
    }

    private void refreshStatusLabel() {
        webSocketManager.getSessionId().ifPresentOrElse(sessionId -> {
            statusLabel.setTextFill(Color.GREEN);
            statusLabel.setText("WS connected: %s".formatted(sessionId));
        }, () -> {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("WS disconnected");
        });
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

    public void onReply(ActionEvent actionEvent) {
        var selectedMessage = inboxTable.getSelectionModel().getSelectedItem();
        var receiverUsername = selectedMessage == null ? null : selectedMessage.getSenderUsername();
        sendMessageDialog.show(receiverUsername, this::refresh);
    }

    @Builder
    private static class TableConfigurer {

        private final TableView<Message> table;
        private final Pagination pagination;
        private final String recipientField;
        private final Function<Pageable, Page<Message>> messagesProvider;

        public void configure() {
            configureColumns();
            var messages = messagesProvider.apply(Pageable.ofSize(25).withPage(pagination.getCurrentPageIndex()));
            pagination.setPageCount(messages.getTotalPages());
            pagination.setVisible(messages.getTotalPages() > 0);
            table.setItems(FXCollections.observableArrayList(messages.getContent()));
        }

        private void configureColumns() {
            var columns = table.getColumns();
            columns.get(0).setCellValueFactory(new PropertyValueFactory<>(recipientField));
            columns.get(1).setCellValueFactory(new PropertyValueFactory<>("sentAt"));
            columns.get(2).setCellValueFactory(new PropertyValueFactory<>("text"));
            columns.get(2).prefWidthProperty().bind(table.widthProperty().subtract(420));
        }
    }
}
