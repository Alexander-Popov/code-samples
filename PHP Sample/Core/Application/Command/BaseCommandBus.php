<?php
declare(strict_types = 1);

namespace app\Core\Application\Command;

final class BaseCommandBus implements CommandBusInterface
{
    /**
     * @param $command
     */
    public function execute($command)
    {
        $handler = $this->resolveHandler($command);
        $handler->handle($command);
    }

    /**
     * @param $command
     * @return object
     * @throws \yii\base\InvalidConfigException
     */
    private function resolveHandler($command)
    {
        return \Yii::createObject(substr(get_class($command), 0, -7) . 'Handler');
    }
}