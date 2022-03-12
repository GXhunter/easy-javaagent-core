package com.gxhunter.agent.core;

public interface AgentConst {
    /**
     * manifest属性
     */
    interface ManiFestAttrKey{
        String ENTRY_NAME = "Hunter-Agent-Plugin-Entry";
        String AUTHOR = "Built-By";
        String VERSION = "Manifest-Version";
        String DESCRIPTION = "Description";
        String PLUGIN_NAME = "Plugin-Name";
    }

    /**
     * 环境变量
     */
    interface SystemEnvKey{
        String ADP_HOST = "adp";
        String DAEMON = "daemon";
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
