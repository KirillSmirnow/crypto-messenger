package cryptomessenger.desktop.infrastructure.ui.controller;

import cryptomessenger.desktop.infrastructure.ui.Refreshable;
import cryptomessenger.desktop.service.dialog.DialogMessage;
import cryptomessenger.desktop.service.dialog.DialogService;
import cryptomessenger.desktop.service.message.MessageService;
import cryptomessenger.desktop.utility.ThreadFactories;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DialogSceneController implements Refreshable {

    private final MessageService messageService;
    private final DialogService dialogService;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, ThreadFactories.daemon());

    public TextArea messageField;

    public String username;
    public TableView<DialogMessage> messagesTable;
    public Pagination messagesTablePagination;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username = resources.getString("otherUsername");
        refresh();
        configureAutoRefresh();
    }

    private void configureAutoRefresh() {
        executor.scheduleWithFixedDelay(() -> Platform.runLater(this::refreshMessagesTable), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void refresh() {
        refreshMessagesTable();
    }

    private void refreshMessagesTable() {
        configureTable();
    }

    public void onSend(ActionEvent actionEvent) {
        messageService.send(username, messageField.getText());
        refreshMessagesTable();
    }

    private void configureTable() {
        configureColumns();
        var messages = dialogService.getMessages(username, Pageable.ofSize(25).withPage(messagesTablePagination.getCurrentPageIndex()));
        messagesTablePagination.setPageCount(messages.getTotalPages());
        messagesTablePagination.setVisible(messages.getTotalPages() > 0);

        messagesTable.setItems(FXCollections.observableArrayList(messages.getContent()));
    }

    private void configureColumns() {
        var columns = messagesTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("sentAt"));
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("inbox"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("outbox"));
    }
}
