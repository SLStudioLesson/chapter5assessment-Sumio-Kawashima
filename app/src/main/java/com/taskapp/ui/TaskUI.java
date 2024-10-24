package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.taskapp.exception.AppException;
import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     *
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");
        System.out.println();

        inputLogin();

        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        taskLogic.showAll(loginUser);
                        selectSubMenu();
                        break;
                    case "2":
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        while (true) {
            try {
                System.out.print("メールアドレスを入力してください：");
                String email = reader.readLine();

                System.out.print("パスワードを入力してください：");
                String password = reader.readLine();

                loginUser = userLogic.login(email, password);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        while (true) {
            try {
                System.out.print("タスクコードを入力してください：");
                String taskCode = reader.readLine();
                if (!isNumeric(taskCode)) {
                    System.out.println("コードは半角の数字で入力してください\n");
                    continue;
                }

                System.out.print("タスク名を入力してください：");
                String name = reader.readLine();
                if (name.length() > 10) {
                    System.out.println("タスク名は10文字以内で入力してください\n");
                    continue;
                }

                System.out.print("担当するユーザーのコードを選択してください：");
                String repUserCode = reader.readLine();
                if (!isNumeric(repUserCode)) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください\n");
                    continue;
                }

                taskLogic.save(Integer.parseInt(taskCode), name, Integer.parseInt(repUserCode), loginUser);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        mainLoop: while (true) {
            try {
                System.out.println("以下1~3から好きな選択肢を選んでください。");
                System.out.println("1. タスクのステータス変更, 2. タスク削除, 3. メインメニューに戻る");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        inputChangeInformation();
                        break;
                    case "2":
                        inputDeleteInformation();
                        break;
                    case "3":
                        System.out.println("メインメニューに戻ります。");
                        break mainLoop;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public void inputChangeInformation() {
        while (true) {
            try {
                System.out.print("ステータスを変更するタスクコードを入力してください：");
                String taskCode = reader.readLine();
                if (!isNumeric(taskCode)) {
                    System.out.println("コードは半角の数字で入力してください\n");
                    continue;
                }

                System.out.println("どのステータスに変更するか選択してください。");
                System.out.println("1. 着手中, 2. 完了");
                System.out.print("選択肢：");
                String status = reader.readLine();
                if (!isNumeric(status)) {
                    System.out.println("ステータスは半角の数字で入力してください\n");
                    continue;
                } else if (!(status.equals("1") || status.equals("2"))) {
                    System.out.println("ステータスは1・2の中から選択してください\n");
                    continue;
                }

                taskLogic.changeStatus(Integer.parseInt(taskCode), Integer.parseInt(status), loginUser);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    public void inputDeleteInformation() {
        while (true) {
            try {
                System.out.print("削除するタスクコードを入力してください：");
                String taskCode = reader.readLine();
                if (!isNumeric(taskCode)) {
                    System.out.println("コードは半角の数字で入力してください\n");
                    continue;
                }

                taskLogic.delete(Integer.parseInt(taskCode));
                break;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        if (inputText == null || inputText.isBlank()) {
            return false;
        } else {
            return inputText.chars().allMatch(c -> Character.isDigit((char) c));
        }
    }
}