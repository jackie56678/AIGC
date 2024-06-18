<template>
  <el-container class="full-height">
    <el-aside width="250px" class="history-container">
      <el-scrollbar class="history-scrollbar">
        <el-button @click="createNewChat" type="primary" class="newchat">开始新聊天</el-button>
        <el-menu :default-active="currentSessionId" @select="handleSelect" class="history-menu">
          <el-menu-item v-for="history in chatHistories" :key="history" :index="history">
            {{ history }}
          </el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container class="chat-container">
      <el-main class="chat-main">
        <el-scrollbar class="chat-scrollbar" ref="chatScroll">
          <div v-if="chats.length" class="chat-messages">
            <div v-for="chat in chats" :key="chat.cid" class="chat-item">
              <p class="user-message"><strong>User:</strong> {{ chat.question }}</p>
              <p class="ai-message" v-if="chat.answer"><strong>AI:</strong> {{ chat.answer }}</p>
            </div>
            <el-button v-if="showNewMessageButton" @click="scrollToBottom" class="new-message-button">⬇ 新消息</el-button>
          </div>
          <div v-else class="no-chat">
            <p>欢迎与BlueLM对话</p>
          </div>
        </el-scrollbar>
      </el-main>

      <el-footer class="chat-footer">
        <el-input v-model="newQuestion" placeholder="输入你的消息" @keyup.enter.native="handleEnter" class="chat-input">
          <el-button slot="prepend" icon="el-icon-microphone" @click="startVoiceInput" class="voice-button"></el-button>
          <el-button slot="append" type="primary" @click="sendChat" class="send-button">发送</el-button>
        </el-input>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script>
import { fetchhistory, fetchchat, sendquestion, newchat } from "@/api/chat"; // Change the path accordingly

export default {
  data() {
    return {
      chatHistories: [],
      chats: [],
      currentSessionId: null,
      newQuestion: "",
      showNewMessageButton: false
    };
  },
  mounted() {
    this.handleInitialPageLoad();
    this.$refs.chatScroll.$el.addEventListener('scroll', this.handleScroll);
  },
  methods: {
    async handleInitialPageLoad() {
      const sessionIdFromURL = this.$route.query.sessionid;
      if (sessionIdFromURL) {
        this.currentSessionId = sessionIdFromURL;
        await this.loadChatHistories();
        await this.loadChat(this.currentSessionId);
      } else {
        await this.loadChatHistories();
      }
    },
    async loadChatHistories() {
      try {
        const response = await fetchhistory();
        this.chatHistories = response.data;
      } catch (error) {
        console.error("Error fetching chat histories:", error);
      }
    },
    async loadChat(sessionid) {
      try {
        const response = await fetchchat(sessionid);
        this.chats = response.data;
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      } catch (error) {
        console.error("Error fetching chat:", error);
      }
    },
    async createNewChat() {
      if (this.$route.query.sessionid) {
        this.$router.push('/ai/chat');
        this.chats = [];
        this.currentSessionId = null
        await this.loadChatHistories();
      }
    },
    async sendChat() {
      if (!this.newQuestion.trim()) return;
      try {
        if (this.currentSessionId) {
          await sendquestion(this.newQuestion, this.currentSessionId);
          this.newQuestion = '';
          await this.loadChat(this.currentSessionId);
        } else {
          const response = await newchat(this.newQuestion);
          this.currentSessionId = response.data.sessionid;
          this.$router.push({ path: '/ai/chat/', query: { sessionid: this.currentSessionId } });
          this.newQuestion = '';
          await this.handleInitialPageLoad();
        }
      } catch (error) {
        console.error("Error sending chat:", error);
      }
    },
    async startVoiceInput() {
      // Implement voice input functionality if needed
    },
    handleSelect(sessionid) {
      this.currentSessionId = sessionid;
      this.$router.push({ path: '/ai/chat', query: { sessionid } });
      this.loadChat(sessionid);
    },
    handleScroll() {
      const chatScroll = this.$refs.chatScroll.$el;
      if (chatScroll.scrollHeight - chatScroll.scrollTop > chatScroll.clientHeight + 50) {
        this.showNewMessageButton = true;
      } else {
        this.showNewMessageButton = false;
      }
    },
    scrollToBottom() {
      const chatScroll = this.$refs.chatScroll.$el;
      chatScroll.scrollTop = chatScroll.scrollHeight;
      this.showNewMessageButton = false;
    },
    handleEnter() {
      if (this.newQuestion.trim()) {
        this.sendChat();
      }
    }
  },
  beforeDestroy() {
    this.$refs.chatScroll.$el.removeEventListener('scroll', this.handleScroll);
  }
};
</script>

<style scoped>
.full-height {
  height: 90vh;
  display: flex;
  border: 1px solid #dcdfe6;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1), 0 6px 20px rgba(0, 0, 0, 0.1);
  background: #fff;
}

.history-container {
  background-color: #1a202c;
  padding: 0;
  display: flex;
  flex-direction: column;
  border-radius: 10px 0 0 10px;
  box-shadow: inset -1px 0 0 rgba(0, 0, 0, 0.1);
}

.history-scrollbar {
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
}

.newchat {
  width: 100%;
  margin-bottom: 20px;
  background-color: #68d391;
  border-color: #48bb78;
  color: #1a202c;
}

.newchat:hover {
  background-color: #48bb78;
  border-color: #38a169;
}

.history-menu {
  margin-top: 20px;
  background-color: transparent;
  border: none;
}

.history-menu .el-menu-item {
  color: #cbd5e0;
  margin: 10px 0;
  padding: 10px 20px;
  border-radius: 5px;
  transition: background-color 0.3s, color 0.3s;
}

.history-menu .el-menu-item:hover,
.history-menu .el-menu-item.is-active {
  background-color: #2d3748;
  color: #68d391;
}

.chat-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  border-radius: 0 10px 10px 0;
  background-color: #f4f6f9;
}

.chat-main {
  padding: 20px;
  overflow: hidden;
  box-sizing: border-box;
  flex: 1;
  position: relative;
  border-radius: 0 10px 0 0;
}

.chat-scrollbar {
  height: 100%;
  overflow-y: auto;
}

.chat-messages {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chat-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
  margin-bottom: 15px;
  border-radius: 10px;
  padding: 15px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s;
}

.chat-item:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.user-message, .ai-message {
  padding: 10px 15px;
  border-radius: 10px;
  font-size: 16px;
}

.user-message {
  background-color: #3182ce;
  color: #fff;
  align-self: flex-start;
  border-bottom-left-radius: 0;
}

.ai-message {
  background-color: #e2e8f0;
  color: #2d3748;
  align-self: flex-end;
  border-bottom-right-radius: 0;
}

.no-chat {
  text-align: center;
  margin-top: 20px;
  color: #718096;
}

.chat-footer {
  background-color: #edf2f7;
  padding: 10px 20px;
  border-radius: 0 0 10px 0;
}

.chat-input {
  width: 100%;
}

.send-button {
  background-color: #68d391;
  border-color: #48bb78;
  color: #1a202c;
}

.send-button:hover {
  background-color: #48bb78;
  border-color: #38a169;
}

.voice-button {
  background-color: #3182ce;
  border-color: #2b6cb0;
  color: #fff;
}

.voice-button:hover {
  background-color: #2b6cb0;
  border-color: #2c5282;
}

.new-message-button {
  position: absolute;
  bottom: 80px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #68d391;
  border-color: #48bb78;
  color: #1a202c;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
</style>
