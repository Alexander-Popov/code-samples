<?php

use yii\db\Migration;
use yii\db\pgsql\Schema;

/**
 * Handles the creation for table `city_table`.
 */
class m160708_090952_create_city_table extends Migration
{
    public function safeUp()
    {
        $tableOptions = null;
        $pk = null;
        if ($this->db->driverName === 'mysql') {
            $tableOptions = 'CHARACTER SET utf8 COLLATE utf8_general_ci ENGINE=InnoDB';
            $pk = Schema::TYPE_STRING . '(36) PRIMARY KEY';
        }

        if ($this->db->driverName === 'pgsql') {
            $pk = 'UUID PRIMARY KEY';
        }

        $this->createTable('{{%city}}', [
            'id' => $pk,
            'name' => $this->string()->notNull(),
            'country' => $this->char(2)->notNull(),
            'extra_charges' => 'NUMERIC(5,2)'
        ], $tableOptions);
        $this->createIndex('unique_index', '{{%city}}', 'name, country', true);
    }

    public function safeDown()
    {
        $this->dropTable('{{%city}}');
    }
}
