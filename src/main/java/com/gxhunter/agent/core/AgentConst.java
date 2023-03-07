package com.gxhunter.agent.core;

public interface AgentConst {
    /**
     * manifest属性
     */
    interface ManiFestAttrKey{
        String ENTRY_NAME = "Hunter-Agent-Plugin-Entry";
        String CLASS_LOADER = "Hunter-Agent-ClassLoader";
        String AUTHOR = "Built-By";
        String VERSION = "Manifest-Version";
        String DESCRIPTION = "Description";
        String PLUGIN_NAME = "Plugin-Name";
    }

    /**
     * 环境变量
     */
    interface SystemEnvKey{
        String AGENT_MD5 = "agent_md5";
        String AGENT_URI = "agent_uri";
    }

    interface LogPrinter{
        /**
         * 分割线
         */
        String DIVIDER = "\n============================================================================\n";
        String NEWLINE = "\n";
    }


    interface ExitCode{
        /**
         * 跳板机错误
         */
        Integer EXIT_SSH_ERROR = 3;
        Integer ERROR_LOADER_PLUGIN = 4;
    }

}
